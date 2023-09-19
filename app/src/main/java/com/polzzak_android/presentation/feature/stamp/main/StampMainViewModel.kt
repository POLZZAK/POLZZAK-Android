package com.polzzak_android.presentation.feature.stamp.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polzzak_android.data.repository.FamilyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StampMainViewModel @Inject constructor(private val familyRepository: FamilyRepository) :
    ViewModel() {

    private val _hasNewRequestLiveData = MutableLiveData<Boolean>()
    val hasNewRequestLiveData: LiveData<Boolean> = _hasNewRequestLiveData
    private var requestHasNewRequestJob: Job? = null

    fun requestHasNewRequest(accessToken: String) {
        requestHasNewRequestJob?.cancel()
        requestHasNewRequestJob = viewModelScope.launch {
            _hasNewRequestLiveData.value = false
            familyRepository.requestLinkRequestStatus(accessToken = accessToken).onSuccess {
                _hasNewRequestLiveData.value =
                    it?.run { isSentRequestUpdated || isReceivedRequestUpdated } == true
            }.onError { _, _ ->
                _hasNewRequestLiveData.value = false
            }
        }
    }
}