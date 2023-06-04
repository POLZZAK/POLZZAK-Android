package com.polzzak_android.presentation.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polzzak_android.common.util.livedata.EventWrapper
import com.polzzak_android.data.repository.LoginRepository
import com.polzzak_android.data.repository.UserRepository
import com.polzzak_android.presentation.auth.login.model.LoginConvertor.toLoginInfoUiModel
import com.polzzak_android.presentation.auth.login.model.LoginInfoUiModel
import com.polzzak_android.presentation.auth.model.SocialLoginType
import com.polzzak_android.presentation.common.model.ApiResult
import com.polzzak_android.presentation.common.util.toApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _loginInfoLiveData = MutableLiveData<EventWrapper<ApiResult<LoginInfoUiModel>>>()
    val loginInfoLiveData: LiveData<EventWrapper<ApiResult<LoginInfoUiModel>>> = _loginInfoLiveData

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
        _loginInfoLiveData.value = EventWrapper(ApiResult.loading())

        val response =
            loginRepository.requestLogin(accessToken = accessToken, loginType = loginType)
        _loginInfoLiveData.value =
            EventWrapper(response.toApiResult { loginResponse -> loginResponse?.toLoginInfoUiModel() })
    }
}