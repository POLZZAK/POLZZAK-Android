package com.polzzak_android.presentation.feature.root.host

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polzzak_android.data.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HostViewModel @Inject constructor(private val notificationRepository: NotificationRepository) :
    ViewModel() {
    private val _hasNewNotificationLiveData = MutableLiveData<Boolean>()
    val hasNewNotificationLiveData: LiveData<Boolean> = _hasNewNotificationLiveData

    private var requestHasNewNotificationJob: Job? = null
    var isSelectedNotificationTab: Boolean = false

    fun requestHasNewNotification(accessToken: String?) {
        if (requestHasNewNotificationJob?.isCompleted == false || accessToken == null) return
        requestHasNewNotificationJob = viewModelScope.launch {
            notificationRepository.requestNotifications(accessToken = accessToken, startId = null)
                .onSuccess {
                    _hasNewNotificationLiveData.value = (it?.unreadNotificationCount ?: 0) > 0
                }
        }
    }
}