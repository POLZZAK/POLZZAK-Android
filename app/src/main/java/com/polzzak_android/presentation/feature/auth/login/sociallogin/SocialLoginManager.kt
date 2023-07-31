package com.polzzak_android.presentation.feature.auth.login.sociallogin

interface SocialLoginManager {
    val googleLoginHelper: GoogleLoginHelper?
    val kakaoLoginHelper: KakaoLoginHelper?
}