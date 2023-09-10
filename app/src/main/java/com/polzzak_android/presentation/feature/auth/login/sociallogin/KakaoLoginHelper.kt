package com.polzzak_android.presentation.feature.auth.login.sociallogin

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import timber.log.Timber

class KakaoLoginHelper(private val context: Context) {
    private var loginSuccessCallbacks: MutableList<(OAuthToken) -> Unit> = mutableListOf()
    private var loginFailedCallbacks: MutableList<() -> Unit> = mutableListOf()

    fun registerLoginSuccessCallback(callback: (token: OAuthToken) -> Unit) {
        loginSuccessCallbacks.add(callback)
    }

    fun registerLoginFailedCallback(callback: () -> Unit) {
        loginFailedCallbacks.add(callback)
    }

    fun unregisterLoginSuccessCallback(callback: (OAuthToken) -> Unit): Boolean {
        return loginSuccessCallbacks.removeIf { it == callback }
    }

    fun unregisterLoginFailedCallback(callback: () -> Unit): Boolean {
        return loginFailedCallbacks.removeIf { it == callback }
    }

    fun requestLogin() {
        with(UserApiClient.instance) {
            val kakaoLoginSuccessCallback: (OAuthToken) -> Unit =
                { token -> loginSuccessCallbacks.forEach { it.invoke(token) } }
            val kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    loginFailedCallbacks.forEach { it.invoke() }
                } else if (token != null) kakaoLoginSuccessCallback(token)
            }

            val kakaoTalkLoginCallback: (OAuthToken?, Throwable?) -> Unit =
                kakaoTalkLoginCallback@{ token, error ->
                    if (error != null) {
                        Timber.e("카카오톡 계정으로 로그인 실패 $error")
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            /*
                             사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                             의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                            */
                            return@kakaoTalkLoginCallback
                        }
                        loginWithKakaoAccount(context, callback = kakaoLoginCallback)
                    } else if (token != null) {
                        Timber.i("카카오톡 계정으로 로그인 성공")
                        kakaoLoginSuccessCallback(token)
                    }
                }

            if (isKakaoTalkLoginAvailable(context)) {
                //카카오톡으로 로그인
                loginWithKakaoTalk(context, callback = kakaoTalkLoginCallback)
            } else {
                //카카오계정으로 로그인
                loginWithKakaoAccount(context, callback = kakaoLoginCallback)
            }
        }
    }

    fun requestLogout() {
        with(UserApiClient.instance) {
            unlink {
                //소셜로그인 로그아웃 콜백
                it?.let {
                    Timber.e("로그아웃 에러 $it")
                }
            }
        }

    }
}