package com.polzzak_android.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polzzak_android.common.model.ApiResult
import com.polzzak_android.common.model.SocialLoginType
import com.polzzak_android.common.model.UserInfo
import com.polzzak_android.data.repository.LoginRepository
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

    fun requestGoogleLogin(id: String, authCode: String) {
        if (loginJob?.isCompleted == false) return
        loginJob = viewModelScope.launch {
            val googleOAuthTokensDeferred =
                async { loginRepository.requestGoogleAccessToken(authCode = authCode) }
            val googleOAuthResponse = googleOAuthTokensDeferred.await()
            val accessToken = googleOAuthResponse.body()?.accessToken
            accessToken?.let {
                requestLogin(id = id, accessToken = it, loginType = SocialLoginType.GOOGLE)
            } ?: run {
                Timber.d("google access token 발급 실패")
                //TODO access token 발급 실패 callback
            }
        }
    }

    fun requestKakaoLogin(id: String, accessToken: String) {
        if (loginJob?.isCompleted == false) return
        loginJob = viewModelScope.launch {
            requestLogin(id = id, accessToken = accessToken, loginType = SocialLoginType.KAKAO)
        }
    }

    private suspend fun requestLogin(id: String, accessToken: String, loginType: SocialLoginType) {
        Timber.d("requestLogin : $id $accessToken $loginType")
        _userInfoLiveData.value = ApiResult.Loading()
        delay(3000)
        //TODO 폴짝 서버에 로그인 요청
    }
}