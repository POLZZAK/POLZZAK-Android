package com.polzzak_android.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.polzzak_android.common.liveData.SingleEvent
import com.polzzak_android.common.model.SocialLoginResult
import com.polzzak_android.common.model.SocialLoginType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _socialLoginResult = MutableLiveData<SingleEvent<SocialLoginResult>>()
    val socialLoginResult: LiveData<SingleEvent<SocialLoginResult>> = _socialLoginResult

    fun setGoogleLoginResult(id: String) {
        _socialLoginResult.value =
            SingleEvent(SocialLoginResult(id = id, loginType = SocialLoginType.GOOGLE))
    }
}