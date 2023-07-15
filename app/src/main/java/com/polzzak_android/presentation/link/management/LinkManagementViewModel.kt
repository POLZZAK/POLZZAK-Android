package com.polzzak_android.presentation.link.management

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.link.management.model.LinkManagementMainTabTypeModel
import com.polzzak_android.presentation.link.model.LinkUserModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LinkManagementViewModel : ViewModel() {
    private val _mainTabTypeLiveData = MutableLiveData(LinkManagementMainTabTypeModel.LINKED)
    val mainTabTypeLiveData: LiveData<LinkManagementMainTabTypeModel> = _mainTabTypeLiveData

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

    fun setMainTabType(tabType: LinkManagementMainTabTypeModel) {
        if (tabType == mainTabTypeLiveData.value) return
        _mainTabTypeLiveData.value = tabType
    }

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
}

private fun getUserMockData(count: Int) = List(count) {
    LinkUserModel(
        userId = it,
        nickName = "name$it",
        profileUrl = "https://picsum.photos/id/${it + 1}/200/300"
    )
}