package com.polzzak_android.data.remote.service

import com.polzzak_android.data.remote.model.request.GoogleOAuthRequest
import com.polzzak_android.data.remote.model.response.GoogleOAuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface GoogleOAuthService {
    @POST("oauth2/v4/token")
    suspend fun requestAccessToken(
        @Body request: GoogleOAuthRequest
    ): Response<GoogleOAuthResponse>
}