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

    private val _linkedUsersLiveData = MutableLiveData<ModelState<List<LinkUserModel>>>()
    val linkedUsersLiveData: LiveData<ModelState<List<LinkUserModel>>> = _linkedUsersLiveData
    private var getLinkedUsersJob: Job? = null

    private val _receivedRequestLiveData = MutableLiveData<ModelState<List<LinkUserModel>>>()
    val receivedRequestLiveData: LiveData<ModelState<List<LinkUserModel>>> =
        _receivedRequestLiveData
    private var getReceivedRequestJob: Job? = null

    private val _sentRequestLiveData = MutableLiveData<ModelState<List<LinkUserModel>>>()
    val sentRequestLiveData: LiveData<ModelState<List<LinkUserModel>>> = _sentRequestLiveData
    private var getSentRequestJob: Job? = null

    private val _deleteLinkLiveData = MutableLiveData<ModelState<String>>()
    val deleteLinkLiveData: LiveData<ModelState<String>> = _deleteLinkLiveData
    private val deleteLinkJobMap: HashMap<Int, Job> = HashMap()

    private val _approveRequestLiveData = MutableLiveData<ModelState<String>>()
    val approveRequestLiveData: LiveData<ModelState<String>> = _approveRequestLiveData
    private val approveRequestJobMap: HashMap<Int, Job> = HashMap()

    init {
        requestLinkedUsers()
        requestSentRequest()
        requestReceivedRequest()
    }

    fun setMainTabType(tabType: LinkManagementMainTabTypeModel) {
        if (tabType == mainTabTypeLiveData.value) return
        _mainTabTypeLiveData.value = tabType
    }

    //링크된 유저
    fun requestLinkedUsers() {
        if (getLinkedUsersJob?.isCompleted == false) return
        getLinkedUsersJob = viewModelScope.launch {
            _linkedUsersLiveData.value = ModelState.Loading()
            delay(300)
            _linkedUsersLiveData.value = ModelState.Success(getUserMockData(17))
        }
    }

    //받은 요청
    fun requestReceivedRequest() {
        if (getReceivedRequestJob?.isCompleted == false) return
        getReceivedRequestJob = viewModelScope.launch {
            _receivedRequestLiveData.value = ModelState.Loading()
            delay(300)
            _receivedRequestLiveData.value = ModelState.Success(getUserMockData(33))
        }
    }

    //보낸 요청
    fun requestSentRequest() {
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
            _deleteLinkLiveData.value = ModelState.Loading(data = linkUserModel.nickName)
            familyRepository.requestDeleteLink(accessToken = accessToken, targetId = userId)
                .onSuccess {
                    _deleteLinkLiveData.value = ModelState.Success(data = linkUserModel.nickName)

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

    //요청 승락
    fun requestApproveLinkRequest(accessToken: String, linkUserModel: LinkUserModel) {
        val userId = linkUserModel.userId
        if (approveRequestJobMap[userId]?.isCompleted == false) return
        approveRequestJobMap[userId] = viewModelScope.launch {
            _approveRequestLiveData.value = ModelState.Loading(data = linkUserModel.nickName)
            familyRepository.requestApproveLinkRequest(accessToken = accessToken, targetId = userId)
                .onSuccess {
                    _approveRequestLiveData.value =
                        ModelState.Success(data = linkUserModel.nickName)

                    val linkedUsers = _linkedUsersLiveData.value?.data ?: return@onSuccess
                    val receivedRequests = _receivedRequestLiveData.value?.data ?: return@onSuccess
                    val updatedLinkedUsers = linkedUsers + linkUserModel
                    val updatedReceivedRequests = receivedRequests.toMutableList().apply {
                        removeIf { user -> user.userId == userId }
                    }

                    _linkedUsersLiveData.value =
                        _linkedUsersLiveData.value?.copyWithData(updatedLinkedUsers)
                    _receivedRequestLiveData.value =
                        _receivedRequestLiveData.value?.copyWithData(updatedReceivedRequests)
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