package com.polzzak_android.presentation.feature.link

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.polzzak_android.data.remote.model.ApiResult
import com.polzzak_android.data.repository.FamilyRepository
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.model.copyWithData
import com.polzzak_android.presentation.feature.link.management.model.LinkManagementMainTabTypeModel
import com.polzzak_android.presentation.feature.link.model.LinkEventType
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

    private val _linkEventLiveData = MutableLiveData<ModelState<LinkEventType>>()
    val linkEventLiveData: LiveData<ModelState<LinkEventType>> = _linkEventLiveData
    private var linkEventJob: Job? = null

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
            }.onError { exception, _ ->
                //TODO 에러처리
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
            }.onError { exception, _ ->
                //TODO 에러처리
            }
        }
    }

    fun requestLinkEvent(
        accessToken: String,
        linkEventType: LinkEventType,
    ) {
        if (linkEventJob?.isCompleted == false) return
        linkEventJob = viewModelScope.launch {
            _linkEventLiveData.value = ModelState.Loading(data = linkEventType)
            getRequestLinkEventResult(
                accessToken = accessToken,
                eventType = linkEventType
            ).onSuccess {
                successLinkEventResult(eventType = linkEventType)
            }.onError { exception, unit ->

            }
        }
    }

    private suspend fun getRequestLinkEventResult(
        accessToken: String,
        eventType: LinkEventType
    ): ApiResult<Unit> {
        val userId = eventType.linkUserModel.userId
        return when (eventType) {
            is LinkEventType.DialogType.RequestLink -> familyRepository::requestLinkRequest
            is LinkEventType.DialogType.DeleteLink -> familyRepository::requestDeleteLink
            is LinkEventType.DialogType.ApproveRequest -> familyRepository::requestApproveLinkRequest
            is LinkEventType.DialogType.CancelRequest -> familyRepository::requestCancelLinkRequest
            is LinkEventType.DialogType.RejectRequest -> familyRepository::requestRejectLinkRequest
            is LinkEventType.CancelRequest -> familyRepository::requestCancelLinkRequest
        }.invoke(accessToken, userId)
    }

    private fun successLinkEventResult(eventType: LinkEventType) {
        _linkEventLiveData.value = ModelState.Success(data = eventType)
        when (eventType) {
            is LinkEventType.DialogType.RequestLink -> successRequestLink(linkUserModel = eventType.linkUserModel)
            is LinkEventType.DialogType.DeleteLink -> successDeleteLink(linkUserModel = eventType.linkUserModel)
            is LinkEventType.DialogType.ApproveRequest -> successApproveRequest(linkUserModel = eventType.linkUserModel)
            is LinkEventType.DialogType.CancelRequest,
            is LinkEventType.CancelRequest -> successCancelRequest(linkUserModel = eventType.linkUserModel)

            is LinkEventType.DialogType.RejectRequest -> successRejectRequest(linkUserModel = eventType.linkUserModel)
        }
    }

    private fun successRequestLink(linkUserModel: LinkUserModel) {
        //보낸 목록 갱신
        val requests = _sentRequestLiveData.value?.data
        if (requests?.any { user -> user.userId == linkUserModel.userId } == false) {
            val updatedSentRequests = requests + linkUserModel
            _sentRequestLiveData.value =
                _sentRequestLiveData.value?.copyWithData(newData = updatedSentRequests)
        }

        //아이디가 같을 경우 유저 검색 결과 갱신
        val updatedLinkRequestUserModel =
            LinkRequestUserModel.Sent(user = linkUserModel)
        updateSearchUserResult(updatedLinkRequestUserModel = updatedLinkRequestUserModel)
    }

    private fun successDeleteLink(linkUserModel: LinkUserModel) {
        val linkedUsers = _linkedUsersLiveData.value?.data ?: return
        val updatedLinkedUsers = linkedUsers.toMutableList().apply {
            removeIf { user -> user.userId == linkUserModel.userId }
        }
        _linkedUsersLiveData.value =
            _linkedUsersLiveData.value?.copyWithData(newData = updatedLinkedUsers)
    }

    private fun successApproveRequest(linkUserModel: LinkUserModel) {
        val linkedUsers = _linkedUsersLiveData.value?.data ?: return
        val receivedRequests =
            _receivedRequestLiveData.value?.data ?: return
        val updatedLinkedUsers = linkedUsers + linkUserModel
        val updatedReceivedRequests = receivedRequests.toMutableList().apply {
            removeIf { user -> user.userId == linkUserModel.userId }
        }

        _linkedUsersLiveData.value =
            _linkedUsersLiveData.value?.copyWithData(updatedLinkedUsers)
        _receivedRequestLiveData.value =
            _receivedRequestLiveData.value?.copyWithData(updatedReceivedRequests)
    }

    private fun successCancelRequest(linkUserModel: LinkUserModel) {
        val sentRequests = _sentRequestLiveData.value?.data ?: return
        val updatedSentRequests = sentRequests.toMutableList().apply {
            removeIf { user -> user.userId == linkUserModel.userId }
        }

        _sentRequestLiveData.value =
            _sentRequestLiveData.value?.copyWithData(updatedSentRequests)

        //아이디가 같을 경우 유저 검색 결과 갱신
        val updatedLinkRequestUserModel =
            LinkRequestUserModel.Normal(user = linkUserModel)
        updateSearchUserResult(updatedLinkRequestUserModel = updatedLinkRequestUserModel)
    }

    private fun successRejectRequest(linkUserModel: LinkUserModel) {
        val receivedRequests =
            _receivedRequestLiveData.value?.data ?: return
        val updatedReceivedRequests = receivedRequests.toMutableList().apply {
            removeIf { user -> user.userId == linkUserModel.userId }
        }

        _receivedRequestLiveData.value =
            _receivedRequestLiveData.value?.copyWithData(updatedReceivedRequests)
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