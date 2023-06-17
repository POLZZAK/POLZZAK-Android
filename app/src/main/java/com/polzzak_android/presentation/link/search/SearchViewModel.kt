package com.polzzak_android.presentation.link.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.polzzak_android.data.repository.FamilyRepository
import com.polzzak_android.presentation.common.model.ModelState
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
import kotlinx.coroutines.delay
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

    private val _requestLiveData = MutableLiveData<ModelState<List<LinkUserModel>>>()
    val requestLiveData: LiveData<ModelState<List<LinkUserModel>>> = _requestLiveData

    private val _cancelLinkLiveData = MutableLiveData<ModelState<Unit?>>()
    val cancelLinkLiveData: LiveData<ModelState<Unit?>> = _cancelLinkLiveData

    private val _searchUserLiveData = MutableLiveData<ModelState<LinkRequestUserModel>>()
    val searchUserLiveData: LiveData<ModelState<LinkRequestUserModel>> = _searchUserLiveData

    private val _requestLinkLiveData = MutableLiveData<ModelState<Unit?>>()
    val requestLinkLiveData: LiveData<ModelState<Unit?>> = _requestLinkLiveData

    private var sentRequestJob: Job? = null
    private var searchUserJob: Job? = null

    init {
        _requestLiveData.value = ModelState.Success(emptyList())
        resetSearchUserResult()
        sentRequestLinks(accessToken = initAccessToken)
    }

    fun setPage(page: LinkPageTypeModel) {
        if (page == pageLiveData.value) return
        _pageLiveData.value = page
    }

    fun setSearchQuery(query: String) {
        _searchQueryLiveData.value = query
    }

    private fun sentRequestLinks(accessToken: String) {
        sentRequestJob?.cancel()
        sentRequestJob = viewModelScope.launch {
            familyRepository.requestSentRequestLinks(accessToken = accessToken).onSuccess {
                val linkUserModel =
                    it?.families?.map { userInfoDto -> userInfoDto.toLinkUserModel() }
                        ?: emptyList()
                _requestLiveData.value = ModelState.Success(data = linkUserModel)
            }.onError { exception, familiesDto ->
                //TODO error hadnling
            }
        }
    }

    fun requestLink(accessToken: String, targetId: Int) {
        viewModelScope.launch {
            _requestLinkLiveData.value = ModelState.Loading()
            familyRepository.requestLink(accessToken = accessToken, targetId = targetId).onSuccess {
                _requestLinkLiveData.value = ModelState.Success(data = it)
            }.onError { exception, _ -> }
        }
    }

    fun cancelRequestLink(accessToken: String, targetId: Int) {
        viewModelScope.launch {
            _cancelLinkLiveData.value = ModelState.Loading()
            familyRepository.requestDeleteLink(accessToken = accessToken, targetId = targetId)
                .onSuccess {
                    _cancelLinkLiveData.value = ModelState.Success(it)
                }.onError { exception, _ ->
                    //TODO error handling

                }
        }
    }

    fun requestSearchUserWithNickName(accessToken: String) {
        searchUserJob?.cancel()
        searchUserJob = viewModelScope.launch {
            _searchUserLiveData.value = ModelState.Loading()
            delay(3000)
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