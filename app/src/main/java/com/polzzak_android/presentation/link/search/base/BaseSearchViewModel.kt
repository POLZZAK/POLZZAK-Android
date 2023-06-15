package com.polzzak_android.presentation.link.search.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polzzak_android.data.repository.FamilyRepository
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.link.search.model.SearchMainRequestModel
import com.polzzak_android.presentation.link.search.model.SearchPageTypeModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseSearchViewModel(
    private val familyRepository: FamilyRepository,
    private val initAccessToken: String
) : ViewModel() {
    private val _pageLiveData = MutableLiveData<SearchPageTypeModel>(SearchPageTypeModel.MAIN)
    val pageLiveData: LiveData<SearchPageTypeModel> = _pageLiveData

    private val _searchQueryLiveData = MutableLiveData<String>("")
    val searchQueryLiveData: LiveData<String> = _searchQueryLiveData

    private val _requestLiveData = MutableLiveData<ModelState<List<SearchMainRequestModel>>>()
    val requestLiveData: LiveData<ModelState<List<SearchMainRequestModel>>> = _requestLiveData

    private val _cancelLinkLiveData = MutableLiveData<ModelState<Nothing?>>()
    val cancelLinkLiveData: LiveData<ModelState<Nothing?>> = _cancelLinkLiveData

//    private val _searchUserLiveData = MutableLiveData<ModelState<User>>()

    private var fetchSentRequestJob: Job? = null
    private var cancelRequestJob: Job? = null
    private var fetchUserJob: Job? = null

    init {
        _requestLiveData.value = ModelState.Success(emptyList())
        requestSentRequestLinks(accessToken = initAccessToken)
    }

    fun setPage(page: SearchPageTypeModel) {
        if (page == pageLiveData.value) return
        _pageLiveData.value = page
    }

    fun setSearchQuery(query: String) {
        _searchQueryLiveData.value = query
    }

    //TODO 이름변경 requestSentLinkRequestUsers
    private fun requestSentRequestLinks(accessToken: String) {
        fetchSentRequestJob?.cancel()
        fetchSentRequestJob = viewModelScope.launch {
            familyRepository.requestSentRequestLinks(accessToken = accessToken).onSuccess {
                val searchMainRequestModel = it?.families?.map { userInfoDto ->
                    SearchMainRequestModel(
                        userId = userInfoDto.memberId,
                        nickName = userInfoDto.nickName,
                        profileUrl = userInfoDto.profileUrl
                    )
                } ?: emptyList()
                _requestLiveData.value = ModelState.Success(data = searchMainRequestModel)
            }.onError { exception, familiesDto ->
                //TODO error hadnling
            }
        }
    }

    fun requestCancelRequestLink(accessToken: String, targetId: Int) {
        if (fetchSentRequestJob?.isCompleted == false) return
        fetchSentRequestJob = viewModelScope.launch {
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
        //TODO 응답 성공 실패 구현
        fetchUserJob?.cancel()
        fetchUserJob = viewModelScope.launch {
            val query = searchQueryLiveData.value ?: ""
            familyRepository.requestUserWithNickName(accessToken = accessToken, nickName = query)
                .onSuccess { }.onError { exception, userInfoDto -> }
        }
    }

    fun cancelSearchUserWithNickNameJob() {
        fetchUserJob?.cancel()
        fetchUserJob = null
        //TODO 모델 초기화
    }
}