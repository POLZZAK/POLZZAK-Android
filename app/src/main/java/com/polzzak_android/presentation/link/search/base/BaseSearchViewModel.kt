package com.polzzak_android.presentation.link.search.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polzzak_android.data.remote.model.ApiException
import com.polzzak_android.data.remote.model.response.UserInfoDto
import com.polzzak_android.data.repository.FamilyRepository
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.link.model.LinkUserModel
import com.polzzak_android.presentation.link.model.LinkPageTypeModel
import com.polzzak_android.presentation.link.model.LinkRequestUserModel
import com.polzzak_android.presentation.link.model.LinkUserStatusModel
import com.polzzak_android.presentation.link.model.toLinkUserModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseSearchViewModel(
    private val familyRepository: FamilyRepository,
    private val initAccessToken: String
) : ViewModel() {
    private val _pageLiveData = MutableLiveData<LinkPageTypeModel>(LinkPageTypeModel.MAIN)
    val pageLiveData: LiveData<LinkPageTypeModel> = _pageLiveData

    private val _searchQueryLiveData = MutableLiveData<String>("")
    val searchQueryLiveData: LiveData<String> = _searchQueryLiveData

    private val _requestLiveData = MutableLiveData<ModelState<List<LinkUserModel>>>()
    val requestLiveData: LiveData<ModelState<List<LinkUserModel>>> = _requestLiveData

    private val _cancelLinkLiveData = MutableLiveData<ModelState<Nothing?>>()
    val cancelLinkLiveData: LiveData<ModelState<Nothing?>> = _cancelLinkLiveData

    private val _searchUserLiveData = MutableLiveData<ModelState<LinkRequestUserModel>>()
    val searchUserLiveData: LiveData<ModelState<LinkRequestUserModel>> = _searchUserLiveData

    private var fetchSentRequestJob: Job? = null
    private var cancelRequestJob: Job? = null
    private var fetchUserJob: Job? = null

    init {
        _requestLiveData.value = ModelState.Success(emptyList())
        requestSentRequestLinks(accessToken = initAccessToken)
    }

    fun setPage(page: LinkPageTypeModel) {
        if (page == pageLiveData.value) return
        _pageLiveData.value = page
    }

    fun setSearchQuery(query: String) {
        _searchQueryLiveData.value = query
    }

    private fun requestSentRequestLinks(accessToken: String) {
        fetchSentRequestJob?.cancel()
        fetchSentRequestJob = viewModelScope.launch {
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
        fetchUserJob?.cancel()
        fetchUserJob = viewModelScope.launch {
            _searchUserLiveData.value = ModelState.Loading()
            val query = searchQueryLiveData.value ?: ""
            familyRepository.requestUserWithNickName(accessToken = accessToken, nickName = query)
                .onSuccess { userInfoDto ->
                    userInfoDto?.let { userInfoDto ->
                        val linkRequestUserModel = LinkRequestUserModel(
                            user = userInfoDto.toLinkUserModel(),
                            status = getUserStatus(userInfoDto = userInfoDto)
                        )
                        _searchUserLiveData.value =
                            ModelState.Success(data = linkRequestUserModel)
                    } ?: run {
                        _searchUserLiveData.value =
                            ModelState.Error(exception = ApiException.UnknownError())
                    }
                }.onError { exception, _ ->
                    _searchUserLiveData.value = ModelState.Error(exception = exception)
                }
        }
    }

    abstract fun getUserStatus(userInfoDto: UserInfoDto?): LinkUserStatusModel

    fun cancelSearchUserWithNickNameJob() {
        fetchUserJob?.cancel()
        fetchUserJob = null
        //TODO 모델 초기화
    }
}