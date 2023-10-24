package com.polzzak_android.presentation.feature.root.host

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HostViewModel : ViewModel() {
    private val _hasNewNotificationLiveData = MutableLiveData<Boolean>()
    val hasNewNotificationLiveData: LiveData<Boolean> = _hasNewNotificationLiveData

    private var requestHasNewNotificationJob: Job? = null
    var isSelectedNotificationTab: Boolean = false

    //TODO 새 알림 존재 여부 api 호출
    fun requestHasNewNotification() {
        if (requestHasNewNotificationJob?.isCompleted == false) return
        requestHasNewNotificationJob = viewModelScope.launch {
            _hasNewNotificationLiveData.value = (0..1).random()==0
        }
    }
}