package com.polzzak_android.presentation.search.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polzzak_android.data.repository.FamilyRepository
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.search.model.SearchMainRequestModel
import com.polzzak_android.presentation.search.model.SearchPageTypeModel
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

    private var fetchSentRequestJob: Job? = null

    init {
        _requestLiveData.value = ModelState.Success(emptyList())
        requestSentRequestLinkUsers(accessToken = initAccessToken)
    }

    fun setPage(page: SearchPageTypeModel) {
        if (page == pageLiveData.value) return
        _pageLiveData.value = page
    }

    fun setSearchQuery(query: String) {
        _searchQueryLiveData.value = query
    }

    private fun requestSentRequestLinkUsers(accessToken: String) {
        fetchSentRequestJob?.cancel()
        fetchSentRequestJob = viewModelScope.launch {
            familyRepository.requestSentRequestLinkUsers(accessToken = accessToken).onSuccess {
                val searchMainRequestModel = it?.families?.map { userInfoDto ->
                    SearchMainRequestModel(
                        userId = userInfoDto.memberId,
                        nickName = userInfoDto.nickName,
                        profileUrl = userInfoDto.profileUrl
                    )
                } ?: emptyList()
                _requestLiveData.value = ModelState.Success(data = searchMainRequestModel)
            }
        }
    }
}