package com.polzzak_android.data.repository

import com.polzzak_android.data.remote.model.response.CheckNickNameValidationResponse
import com.polzzak_android.data.remote.service.AuthService
import retrofit2.Response
import javax.inject.Inject

class SignUpRepository @Inject constructor(
    private val authService: AuthService
) {
    suspend fun requestCheckNickNameValidation(
        nickName: String
    ): Response<CheckNickNameValidationResponse> {
        return authService.requestCheckNickNameValidation(nickName = nickName)
    }
}