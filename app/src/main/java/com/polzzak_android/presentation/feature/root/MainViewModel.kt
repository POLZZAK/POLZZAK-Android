package com.polzzak_android.presentation.feature.root

import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var accessToken: String? = null

    fun logout() {
        accessToken = null
    }
}