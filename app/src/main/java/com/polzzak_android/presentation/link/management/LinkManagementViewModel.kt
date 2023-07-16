package com.polzzak_android.presentation.link.management

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.polzzak_android.data.repository.FamilyRepository
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.model.copyWithData
import com.polzzak_android.presentation.link.management.model.LinkManagementMainTabTypeModel
import com.polzzak_android.presentation.link.management.model.LinkManagementRequestStatusModel
import com.polzzak_android.presentation.link.management.model.toLinkManagementRequestStatusModel
import com.polzzak_android.presentation.link.model.LinkUserModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LinkManagementViewModel @AssistedInject constructor(
    private val familyRepository: FamilyRepository,
    @Assisted private val initAccessToken: String
) : ViewModel() {
    private val _mainTabTypeLiveData = MutableLiveData(LinkManagementMainTabTypeModel.LINKED)
    val mainTabTypeLiveData: LiveData<LinkManagementMainTabTypeModel> = _mainTabTypeLiveData

    private val _requestStatusLiveData = MutableLiveData<ModelState<LinkManagementRequestStatusModel>>()
    val requestStatusLiveData: LiveData<ModelState<LinkManagementRequestStatusModel>> = _requestStatusLiveData
    private var requestStatusJob: Job? = null

    private val _linkedUsersLiveData = MutableLiveData<ModelState<List<LinkUserModel>>>()
    val linkedUsersLiveData: LiveData<ModelState<List<LinkUserModel>>> = _linkedUsersLiveData
    private var isLinkedUsersInitialized = false
    private var getLinkedUsersJob: Job? = null

    private val _receivedRequestLiveData = MutableLiveData<ModelState<List<LinkUserModel>>>()
    val receivedRequestLiveData: LiveData<ModelState<List<LinkUserModel>>> =
        _receivedRequestLiveData
    private var isReceivedRequestInitialized = false
    private var getReceivedRequestJob: Job? = null

    private val _sentRequestLiveData = MutableLiveData<ModelState<List<LinkUserModel>>>()
    val sentRequestLiveData: LiveData<ModelState<List<LinkUserModel>>> = _sentRequestLiveData
    private var isSentRequestInitialized = false
    private var getSentRequestJob: Job? = null

    private val _deleteLinkLiveData = MutableLiveData<ModelState<Unit?>>()
    val deleteLinkLiveData: LiveData<ModelState<Unit?>> = _deleteLinkLiveData
    private val deleteLinkJobMap: HashMap<Int, Job> = HashMap()

    init {
        requestRequestStatus(accessToken = initAccessToken)
    }

    fun setMainTabType(tabType: LinkManagementMainTabTypeModel) {
        if (tabType == mainTabTypeLiveData.value) return
        _mainTabTypeLiveData.value = tabType
    }

    fun requestRequestStatus(accessToken: String) {
        if (requestStatusJob?.isCompleted == false) return
        requestStatusJob = viewModelScope.launch {
            familyRepository.requestLinkRequestStatus(accessToken = accessToken).onSuccess {
                _requestStatusLiveData.value =
                    ModelState.Success(it.toLinkManagementRequestStatusModel())
            }.onError { exception, _ ->
                //TODO 에러처리
            }
        }
    }

    //링크된 유저
    fun requestLinkedUsers() {
        if (isLinkedUsersInitialized) return
        isLinkedUsersInitialized = true
        if (getLinkedUsersJob?.isCompleted == false) return
        getLinkedUsersJob = viewModelScope.launch {
            _linkedUsersLiveData.value = ModelState.Loading()
            delay(300)
            _linkedUsersLiveData.value = ModelState.Success(getUserMockData(17))
        }
    }

    //받은 요청
    fun requestReceivedRequest() {
        if (isReceivedRequestInitialized) return
        isReceivedRequestInitialized = true
        if (getReceivedRequestJob?.isCompleted == false) return
        getReceivedRequestJob = viewModelScope.launch {
            _receivedRequestLiveData.value = ModelState.Loading()
            delay(300)
            _receivedRequestLiveData.value = ModelState.Success(getUserMockData(33))
        }
    }

    //보낸 요청
    fun requestSentRequest() {
        if (isSentRequestInitialized) return
        isSentRequestInitialized = true
        if (getSentRequestJob?.isCompleted == false) return
        getSentRequestJob = viewModelScope.launch {
            _sentRequestLiveData.value = ModelState.Loading()
            delay(300)
            _sentRequestLiveData.value = ModelState.Success(getUserMockData(21))
        }
    }

    //링크 삭제
    fun requestDeleteLink(accessToken: String, linkUserModel: LinkUserModel) {
        val userId = linkUserModel.userId
        if (deleteLinkJobMap[userId]?.isCompleted == false) return
        deleteLinkJobMap[userId] = viewModelScope.launch {
            familyRepository.requestDeleteLink(accessToken = accessToken, targetId = userId)
                .onSuccess {
                    _deleteLinkLiveData.value = ModelState.Success(it)

                    val linkedUsers = _linkedUsersLiveData.value?.data ?: return@onSuccess
                    val updatedLinkedUsers = linkedUsers.toMutableList().apply {
                        removeIf { user -> user.userId == userId }
                    }
                    _linkedUsersLiveData.value =
                        _linkedUsersLiveData.value?.copyWithData(newData = updatedLinkedUsers)
                }.onError { exception, _ ->
                    //TODO 에러처리
                }
        }
    }

    @AssistedFactory
    interface LinkManagementViewModelAssistedFactory {
        fun create(
            initAccessToken: String,
        ): LinkManagementViewModel
    }

    companion object {
        fun provideFactory(
            linkManagementViewModelAssistedFactory: LinkManagementViewModelAssistedFactory,
            initAccessToken: String,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return linkManagementViewModelAssistedFactory.create(
                    initAccessToken = initAccessToken
                ) as T
            }
        }
    }
}

private fun getUserMockData(count: Int) = List(count) {
    LinkUserModel(
        userId = it,
        nickName = "name$it",
        profileUrl = "https://picsum.photos/id/${it + 1}/200/300"
    )
}