package com.polzzak_android.data.repository

import com.polzzak_android.BuildConfig
import com.polzzak_android.common.model.SocialLoginType
import com.polzzak_android.common.model.UserInfo
import com.polzzak_android.data.remote.model.request.GoogleOAuthRequest
import com.polzzak_android.data.remote.model.response.GoogleOAuthResponse
import com.polzzak_android.data.remote.service.GoogleOAuthService
import com.polzzak_android.data.remote.service.LoginService
import retrofit2.Response
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val loginService: LoginService,
    private val googleTokenService: GoogleOAuthService
) {
    suspend fun requestLogin(
        id: String,
        accessToken: String,
        loginType: SocialLoginType
    ): Response<UserInfo> {
        return Response.success(mockData)
    }

    suspend fun requestGoogleAccessToken(authCode: String): Response<GoogleOAuthResponse> {
        val request = GoogleOAuthRequest(
            grantType = "authorization_code",
            clientId = BuildConfig.GOOGLE_WEB_APPLICATION_CLIENT_ID,
            clientSecret = BuildConfig.GOOGLE_WEB_APPLICATION_CLIENT_PWD,
            accessType = "offline",
            code = authCode
        )
        return googleTokenService.requestAccessToken(request)
    }

    private fun SocialLoginType.toRequestStr() = when (this) {
        SocialLoginType.KAKAO -> "kakao"
        SocialLoginType.GOOGLE -> "google"
    }
}

private val mockData = UserInfo(nickName = "test1", userType = "google", profileUrl = null)