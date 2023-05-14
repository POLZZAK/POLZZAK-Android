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

    fun requestGoogleLogin(id: String, authCode: String) {
        if (loginJob?.isCompleted == false) return
        loginJob = viewModelScope.launch {
            //TODO 로컬에 저장한 뒤 없을 경우 요청하도록 변경
            val googleOAuthTokensDeferred =
                async { loginRepository.requestGoogleAccessToken(authCode = authCode) }
            val googleOAuthResponse = googleOAuthTokensDeferred.await()
            Timber.d("response = ${googleOAuthResponse.body()}")

            requestLogin(id = id, accessToken = "", loginType = SocialLoginType.GOOGLE)
        }
    }

    fun requestKakaoLogin(id: String, accessToken: String) {
        if (loginJob?.isCompleted == false) return
        loginJob = viewModelScope.launch {
            requestLogin(id = id, accessToken = accessToken, loginType = SocialLoginType.KAKAO)
        }
    }

    private suspend fun requestLogin(id: String, accessToken: String, loginType: SocialLoginType) {
        _userInfoLiveData.value = ApiResult.Loading()
        delay(3000)
        //TODO 폴짝 서버에 로그인 요청
    }
}