package com.polzzak_android.presentation.feature.root

import androidx.lifecycle.ViewModel
import com.polzzak_android.presentation.common.service.PolzzakFirebaseMessagingService

class MainViewModel : ViewModel() {
    var accessToken: String? = null
        private set

    fun logout() {
        accessToken = null
        PolzzakFirebaseMessagingService.receiveMessage = false
    }

    fun login(accessToken: String) {
        this.accessToken = accessToken
        PolzzakFirebaseMessagingService.receiveMessage = true
    }
}