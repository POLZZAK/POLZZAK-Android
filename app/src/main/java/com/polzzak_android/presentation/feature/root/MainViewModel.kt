package com.polzzak_android.presentation.feature.root

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.polzzak_android.common.util.livedata.EventWrapper
import com.polzzak_android.presentation.common.service.PolzzakFirebaseMessagingService

class MainViewModel : ViewModel() {
    var accessToken: String? = null
        private set

    private val _moveNotificationTabLiveEvent = MutableLiveData<EventWrapper<Boolean>>()
    val moveNotificationTabLiveEvent: LiveData<EventWrapper<Boolean>> =
        _moveNotificationTabLiveEvent

    private val _refreshNotificationLiveEvent = MutableLiveData<EventWrapper<Boolean>>()
    val refreshNotificationLiveEvent: LiveData<EventWrapper<Boolean>> =
        _refreshNotificationLiveEvent

    fun logout() {
        accessToken = null
        PolzzakFirebaseMessagingService.receiveMessage = false
    }

    fun login(accessToken: String) {
        this.accessToken = accessToken
        PolzzakFirebaseMessagingService.receiveMessage = true
    }

    fun clickNotificationMessage() {
        _moveNotificationTabLiveEvent.value = EventWrapper(true)
    }

    fun refreshNotifications() {
        _refreshNotificationLiveEvent.value = EventWrapper(true)
    }
}