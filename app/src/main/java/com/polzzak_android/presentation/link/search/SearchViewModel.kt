package com.polzzak_android.presentation.link.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.polzzak_android.data.repository.FamilyRepository
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.model.copyWithData
import com.polzzak_android.presentation.link.model.LinkMemberType
import com.polzzak_android.presentation.link.model.LinkPageTypeModel
import com.polzzak_android.presentation.link.model.LinkRequestUserModel
import com.polzzak_android.presentation.link.model.LinkUserModel
import com.polzzak_android.presentation.link.model.toLinkRequestUserModel
import com.polzzak_android.presentation.link.model.toLinkUserModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchViewModel @AssistedInject constructor(
    private val familyRepository: FamilyRepository,
    @Assisted private val initAccessToken: String,
    @Assisted("userType") private val linkMemberType: LinkMemberType,
    @Assisted("targetType") private val targetLinkMemberType: LinkMemberType
) : ViewModel() {
    private val _pageLiveData = MutableLiveData<LinkPageTypeModel>(LinkPageTypeModel.MAIN)
    val pageLiveData: LiveData<LinkPageTypeModel> = _pageLiveData

    private val _searchQueryLiveData = MutableLiveData<String>("")
    val searchQueryLiveData: LiveData<String> = _searchQueryLiveData

    private val _requestSentLiveData = MutableLiveData<ModelState<List<LinkUserModel>>>()
    val requestSentLiveData: LiveData<ModelState<List<LinkUserModel>>> = _requestSentLiveData
    private var sentRequestJob: Job? = null

    private val _cancelLinkLiveData = MutableLiveData<ModelState<Unit?>>()
    val cancelLinkLiveData: LiveData<ModelState<Unit?>> = _cancelLinkLiveData
    private val cancelLinkJobMap: HashMap<Int, Job> = HashMap()

    private val _searchUserLiveData = MutableLiveData<ModelState<LinkRequestUserModel>>()
    val searchUserLiveData: LiveData<ModelState<LinkRequestUserModel>> = _searchUserLiveData
    private var searchUserJob: Job? = null

    private val _requestLinkLiveData = MutableLiveData<ModelState<Unit?>>()
    val requestLinkLiveData: LiveData<ModelState<Unit?>> = _requestLinkLiveData
    private val linkJobMap: HashMap<Int, Job> = HashMap()

    init {
        _requestSentLiveData.value = ModelState.Success(emptyList())
        resetSearchUserResult()
        requestSentRequestLinks(accessToken = initAccessToken)
    }

    fun setPage(page: LinkPageTypeModel) {
        if (page == pageLiveData.value) return
        _pageLiveData.value = page
    }

    fun setSearchQuery(query: String) {
        _searchQueryLiveData.value = query
    }

    //보낸목록 요청
    private fun requestSentRequestLinks(accessToken: String) {
        sentRequestJob?.cancel()
        sentRequestJob = viewModelScope.launch {
            familyRepository.requestSentRequestLinks(accessToken = accessToken).onSuccess {
                val linkUserModel =
                    it?.families?.map { userInfoDto -> userInfoDto.toLinkUserModel() }
                        ?: emptyList()
                _requestSentLiveData.value = ModelState.Success(data = linkUserModel)
            }.onError { exception, familiesDto ->
                //TODO error hadnling
            }
        }
    }

    //연동 요청
    fun requestLink(accessToken: String, linkUserModel: LinkUserModel) {
        val userId = linkUserModel.userId
        if (linkJobMap[userId]?.isCompleted == false) return
        linkJobMap[userId] = viewModelScope.launch {
            _requestLinkLiveData.value = ModelState.Loading()
            familyRepository.requestLinkRequest(
                accessToken = accessToken,
                targetId = userId
            ).onSuccess {
                _requestLinkLiveData.value = ModelState.Success(data = it)

                //보낸 목록 갱신
                val requests = _requestSentLiveData.value?.data
                if (requests?.any { user -> user.userId == userId } == false) {
                    val updatedSentRequests = requests + listOf(linkUserModel)
                    _requestSentLiveData.value =
                        _requestSentLiveData.value?.copyWithData(newData = updatedSentRequests)
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

    //연동 취소 요청
    fun requestCancelRequestLink(accessToken: String, linkUserModel: LinkUserModel) {
        val userId = linkUserModel.userId
        if (cancelLinkJobMap[userId]?.isCompleted == false) return
        cancelLinkJobMap[userId] = viewModelScope.launch {
            _cancelLinkLiveData.value = ModelState.Loading()
            familyRepository.requestCancelLinkRequest(
                accessToken = accessToken,
                targetId = userId
            ).onSuccess {
                _cancelLinkLiveData.value = ModelState.Success(it)

                //보낸 목록 갱신
                val requests = _requestSentLiveData.value?.data ?: return@onSuccess
                val updatedSentRequests = requests.toMutableList().apply {
                    removeIf { user -> user.userId == userId }
                }
                _requestSentLiveData.value =
                    _requestSentLiveData.value?.copyWithData(newData = updatedSentRequests)

                //아이디가 같을 경우 유저 검색 결과 갱신
                val updatedLinkRequestUserModel = LinkRequestUserModel.Normal(user = linkUserModel)
                updateSearchUserResult(updatedLinkRequestUserModel = updatedLinkRequestUserModel)
            }.onError { exception, _ ->
                //TODO error handling
            }
        }.apply {
            invokeOnCompletion {
                cancelLinkJobMap.remove(userId)
            }
        }
    }

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

    fun cancelSearchUserWithNickNameJob() {
        searchUserJob?.cancel()
        searchUserJob = null
        resetSearchUserResult()
    }

    private fun resetSearchUserResult() {
        _searchUserLiveData.value =
            ModelState.Success(LinkRequestUserModel.Guide(targetLinkMemberType = targetLinkMemberType))
    }

    private fun updateSearchUserResult(updatedLinkRequestUserModel: LinkRequestUserModel) {
        val linkRequestUserModelState = searchUserLiveData.value
        if (linkRequestUserModelState?.data?.user?.userId != updatedLinkRequestUserModel.user?.userId) return
        if (searchUserJob?.isCompleted == false) return
        _searchUserLiveData.value =
            linkRequestUserModelState?.copyWithData(newData = updatedLinkRequestUserModel)
    }

    @AssistedFactory
    interface SearchViewModelAssistedFactory {
        fun create(
            initAccessToken: String,
            @Assisted("userType") linkMemberType: LinkMemberType,
            @Assisted("targetType") targetLinkMemberType: LinkMemberType
        ): SearchViewModel
    }

    companion object {
        fun provideFactory(
            searchViewModelAssistedFactory: SearchViewModelAssistedFactory,
            initAccessToken: String,
            linkMemberType: LinkMemberType,
            targetLinkMemberType: LinkMemberType
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return searchViewModelAssistedFactory.create(
                    initAccessToken = initAccessToken,
                    linkMemberType = linkMemberType,
                    targetLinkMemberType = targetLinkMemberType
                ) as T
            }
        }
    }
}