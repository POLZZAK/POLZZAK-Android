package com.polzzak_android.presentation.auth.login.sociallogin

interface SocialLoginManager {
    val googleLoginHelper: GoogleLoginHelper?
    val kakaoLoginHelper: KakaoLoginHelper?
}