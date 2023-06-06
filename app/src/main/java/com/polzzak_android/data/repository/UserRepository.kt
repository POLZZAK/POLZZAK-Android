package com.polzzak_android.data.repository

import com.polzzak_android.data.remote.model.response.UserResponse
import com.polzzak_android.data.remote.service.UserService
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userService: UserService
) {
    suspend fun requestUser(accessToken: String): Response<UserResponse> {
        return userService.requestUserInfo(accessToken = accessToken)
    }
}