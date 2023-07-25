package com.polzzak_android.presentation.feature.link

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.polzzak_android.data.repository.FamilyRepository
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.model.copyWithData
import com.polzzak_android.presentation.feature.link.management.model.LinkManagementMainTabTypeModel
import com.polzzak_android.presentation.feature.link.model.LinkMemberType
import com.polzzak_android.presentation.feature.link.model.LinkPageTypeModel
import com.polzzak_android.presentation.feature.link.model.LinkRequestUserModel
import com.polzzak_android.presentation.feature.link.model.LinkUserModel
import com.polzzak_android.presentation.feature.link.model.toLinkRequestUserModel
import com.polzzak_android.presentation.feature.link.model.toLinkUserModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LinkViewModel @AssistedInject constructor(
    private val familyRepository: FamilyRepository,
    @Assisted private val initAccessToken: String,
    @Assisted("userType") private val linkMemberType: LinkMemberType,
    @Assisted("targetType") private val targetLinkMemberType: LinkMemberType
) : ViewModel() {
    private val _pageLiveData = MutableLiveData<LinkPageTypeModel>(LinkPageTypeModel.MAIN)
    val pageLiveData: LiveData<LinkPageTypeModel> = _pageLiveData

    private val _searchQueryLiveData = MutableLiveData<String>("")
    val searchQueryLiveData: LiveData<String> = _searchQueryLiveData

    private val _searchUserLiveData = MutableLiveData<ModelState<LinkRequestUserModel>>()
    val searchUserLiveData: LiveData<ModelState<LinkRequestUserModel>> = _searchUserLiveData
    private var searchUserJob: Job? = null

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

    private val _rejectRequestLiveData = MutableLiveData<ModelState<String>>()
    val rejectRequestLiveData: LiveData<ModelState<String>> = _rejectRequestLiveData
    private val rejectRequestJobMap: HashMap<Int, Job> = HashMap()

    private val _cancelRequestLiveData = MutableLiveData<ModelState<String>>()
    val cancelRequestLiveData: LiveData<ModelState<String>> = _rejectRequestLiveData
    private val cancelRequestJobMap: HashMap<Int, Job> = HashMap()

    private val _requestLinkLiveData = MutableLiveData<ModelState<String>>()
    val requestLinkLiveData: LiveData<ModelState<String>> = _requestLinkLiveData
    private val linkJobMap: HashMap<Int, Job> = HashMap()

    init {
        requestLinkedUsers(accessToken = initAccessToken)
        requestSentRequest(accessToken = initAccessToken)
        requestReceivedRequest(accessToken = initAccessToken)
        resetSearchUserResult()
    }

    fun setPage(page: LinkPageTypeModel) {
        if (page == pageLiveData.value) return
        _pageLiveData.value = page
        if (page == LinkPageTypeModel.REQUEST) resetSearchUserResult()
    }

    fun setSearchQuery(query: String) {
        _searchQueryLiveData.value = query
    }

    fun setMainTabType(tabType: LinkManagementMainTabTypeModel) {
        if (tabType == mainTabTypeLiveData.value) return
        _mainTabTypeLiveData.value = tabType
    }

    //유저검색
    fun requestSearchUserWithNickName(accessToken: String) {
        searchUserJob?.cancel()
        searchUserJob = viewModelScope.launch {
            _searchUserLiveData.value = ModelState.Loading()
            val query = searchQueryLiveData.value ?: ""
            familyRepository.requestUserWithNickName(accessToken = accessToken, nickName = query)
                .onSuccess { userInfoDto ->
                    val linkRequestUserModel =
                        userInfoDto.toLinkRequestUserModel(
                            nickName = query,
                            linkMemberType = linkMemberType
                        )
                    _searchUserLiveData.value =
                        ModelState.Success(data = linkRequestUserModel)
                }.onError { exception, _ ->
                    _searchUserLiveData.value = ModelState.Error(exception = exception)
                }
        }
    }

    //링크된 유저
    private fun requestLinkedUsers(accessToken: String) {
        if (getLinkedUsersJob?.isCompleted == false) return
        getLinkedUsersJob = viewModelScope.launch {
            _linkedUsersLiveData.value = ModelState.Loading()
            familyRepository.requestLinkedUsers(accessToken = accessToken).onSuccess {
                val data = it?.families?.map { userInfoDto -> userInfoDto.toLinkUserModel() }
                    ?: emptyList()
                _linkedUsersLiveData.value = ModelState.Success(data = data)
            }.onError { exception, _ ->
                //TODO 에러처리
            }
        }
    }

    //받은 요청
    private fun requestReceivedRequest(accessToken: String) {
        if (getReceivedRequestJob?.isCompleted == false) return
        getReceivedRequestJob = viewModelScope.launch {
            _receivedRequestLiveData.value = ModelState.Loading()
            familyRepository.requestReceivedRequestLinks(accessToken = accessToken).onSuccess {
                val data = it?.families?.map { userInfoDto -> userInfoDto.toLinkUserModel() }
                    ?: emptyList()
                _receivedRequestLiveData.value = ModelState.Success(data = data)
            }
        }
    }

    //보낸 요청
    private fun requestSentRequest(accessToken: String) {
        if (getSentRequestJob?.isCompleted == false) return
        getSentRequestJob = viewModelScope.launch {
            _sentRequestLiveData.value = ModelState.Loading()
            familyRepository.requestSentRequestLinks(accessToken = accessToken).onSuccess {
                val data = it?.families?.map { userInfoDto -> userInfoDto.toLinkUserModel() }
                    ?: emptyList()
                _sentRequestLiveData.value = ModelState.Success(data = data)
            }
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
        }.apply {
            invokeOnCompletion {
                deleteLinkJobMap.remove(userId)
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
        }.apply {
            invokeOnCompletion {
                approveRequestJobMap.remove(userId)
            }
        }
    }

    //요청 거절
    fun requestRejectLinkRequest(accessToken: String, linkUserModel: LinkUserModel) {
        val userId = linkUserModel.userId
        if (rejectRequestJobMap[userId]?.isCompleted == false) return
        rejectRequestJobMap[userId] = viewModelScope.launch {
            _rejectRequestLiveData.value = ModelState.Loading(data = linkUserModel.nickName)
            familyRepository.requestRejectLinkRequest(accessToken = accessToken, targetId = userId)
                .onSuccess {
                    _rejectRequestLiveData.value =
                        ModelState.Success(data = linkUserModel.nickName)

                    val receivedRequests = _receivedRequestLiveData.value?.data ?: return@onSuccess
                    val updatedReceivedRequests = receivedRequests.toMutableList().apply {
                        removeIf { user -> user.userId == userId }
                    }

                    _receivedRequestLiveData.value =
                        _receivedRequestLiveData.value?.copyWithData(updatedReceivedRequests)
                }.onError { exception, _ ->
                    //TODO 에러처리
                }
        }.apply {
            invokeOnCompletion {
                rejectRequestJobMap.remove(userId)
            }
        }
    }

    //보낸 요청 취소
    fun requestCancelLinkRequest(accessToken: String, linkUserModel: LinkUserModel) {
        val userId = linkUserModel.userId
        if (cancelRequestJobMap[userId]?.isCompleted == false) return
        cancelRequestJobMap[userId] = viewModelScope.launch {
            _cancelRequestLiveData.value = ModelState.Loading(data = linkUserModel.nickName)
            familyRepository.requestCancelLinkRequest(accessToken = accessToken, targetId = userId)
                .onSuccess {
                    _cancelRequestLiveData.value =
                        ModelState.Success(data = linkUserModel.nickName)

                    val sentRequests = _sentRequestLiveData.value?.data ?: return@onSuccess
                    val updatedSentRequests = sentRequests.toMutableList().apply {
                        removeIf { user -> user.userId == userId }
                    }

                    _sentRequestLiveData.value =
                        _sentRequestLiveData.value?.copyWithData(updatedSentRequests)

                    //아이디가 같을 경우 유저 검색 결과 갱신
                    val updatedLinkRequestUserModel =
                        LinkRequestUserModel.Normal(user = linkUserModel)
                    updateSearchUserResult(updatedLinkRequestUserModel = updatedLinkRequestUserModel)
                }.onError { exception, _ ->
                    //TODO 에러처리
                }
        }.apply {
            invokeOnCompletion {
                cancelRequestJobMap.remove(userId)
            }
        }
    }

    //연동 요청
    fun requestLink(accessToken: String, linkUserModel: LinkUserModel) {
        val userId = linkUserModel.userId
        val nickName = linkUserModel.nickName
        if (linkJobMap[userId]?.isCompleted == false) return
        linkJobMap[userId] = viewModelScope.launch {
            _requestLinkLiveData.value = ModelState.Loading(data = nickName)
            familyRepository.requestLinkRequest(
                accessToken = accessToken,
                targetId = userId
            ).onSuccess {
                _requestLinkLiveData.value = ModelState.Success(data = nickName)

                //보낸 목록 갱신
                val requests = _sentRequestLiveData.value?.data
                if (requests?.any { user -> user.userId == userId } == false) {
                    val updatedSentRequests = requests + listOf(linkUserModel)
                    _sentRequestLiveData.value =
                        _sentRequestLiveData.value?.copyWithData(newData = updatedSentRequests)
                }

                //아이디가 같을 경우 유저 검색 결과 갱신
                val updatedLinkRequestUserModel =
                    LinkRequestUserModel.Sent(user = linkUserModel)
                updateSearchUserResult(updatedLinkRequestUserModel = updatedLinkRequestUserModel)

            }.onError { exception, _ -> }
        }.apply {
            invokeOnCompletion {
                linkJobMap.remove(userId)
            }
        }
    }

    private fun updateSearchUserResult(updatedLinkRequestUserModel: LinkRequestUserModel) {
        val linkRequestUserModelState = searchUserLiveData.value
        if (linkRequestUserModelState?.data?.user?.userId != updatedLinkRequestUserModel.user?.userId) return
        if (searchUserJob?.isCompleted == false) return
        _searchUserLiveData.value =
            linkRequestUserModelState?.copyWithData(newData = updatedLinkRequestUserModel)
    }

    fun cancelSearchUserWithNickNameJob() {
        searchUserJob?.cancel()
        searchUserJob = null
        resetSearchUserResult()
    }

    fun resetSearchUserResult() {
        _searchUserLiveData.value =
            ModelState.Success(LinkRequestUserModel.Guide(targetLinkMemberType = targetLinkMemberType))
    }

    @AssistedFactory
    interface LinkViewModelAssistedFactory {
        fun create(
            initAccessToken: String,
            @Assisted("userType") linkMemberType: LinkMemberType,
            @Assisted("targetType") targetLinkMemberType: LinkMemberType
        ): LinkViewModel
    }

    companion object {
        fun provideFactory(
            linkViewModelAssistedFactory: LinkViewModelAssistedFactory,
            initAccessToken: String,
            linkMemberType: LinkMemberType,
            targetLinkMemberType: LinkMemberType
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return linkViewModelAssistedFactory.create(
                    initAccessToken = initAccessToken,
                    linkMemberType = linkMemberType,
                    targetLinkMemberType = targetLinkMemberType
                ) as T
            }
        }
    }
}