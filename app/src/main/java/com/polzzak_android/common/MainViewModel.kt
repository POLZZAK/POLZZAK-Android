package com.polzzak_android.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polzzak_android.common.model.ApiResult
import com.polzzak_android.common.model.SocialLoginType
import com.polzzak_android.common.model.UserInfo
import com.polzzak_android.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
) :
    ViewModel() {
    private val _userInfoLiveData = MutableLiveData<ApiResult<UserInfo>>()
    val userInfoLiveData: LiveData<ApiResult<UserInfo>> = _userInfoLiveData

    private var loginJob: Job? = null

    fun requestGoogleLogin(authCode: String) {
        if (loginJob?.isCompleted == false) return
        loginJob = viewModelScope.launch {
            val googleOAuthTokensDeferred =
                async { loginRepository.requestGoogleAccessToken(authCode = authCode) }
            val googleOAuthResponse = googleOAuthTokensDeferred.await()
            val accessToken = googleOAuthResponse.body()?.accessToken
            accessToken?.let {
                requestLogin(accessToken = it, loginType = SocialLoginType.GOOGLE)
            } ?: run {
                Timber.d("google access token 발급 실패")
                //TODO access token 발급 실패 callback
            }
        }
    }

    fun requestKakaoLogin(accessToken: String) {
        if (loginJob?.isCompleted == false) return
        loginJob = viewModelScope.launch {
            requestLogin(accessToken = accessToken, loginType = SocialLoginType.KAKAO)
        }
    }

    private suspend fun requestLogin(accessToken: String, loginType: SocialLoginType) {
        _userInfoLiveData.value = ApiResult.Loading()
        delay(3000)
        //TODO 폴짝 서버에 로그인 요청
        loginRepository.requestLogin(accessToken = accessToken, loginType = loginType).let{response->
            Timber.d("${response.errorBody()?.string()}")
        }
    }
}