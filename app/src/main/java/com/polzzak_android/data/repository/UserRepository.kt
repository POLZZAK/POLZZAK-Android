package com.polzzak_android.data.repository

import com.polzzak_android.data.remote.model.ApiResult
import com.polzzak_android.data.remote.model.response.UserResponse
import com.polzzak_android.data.remote.service.UserService
import com.polzzak_android.data.remote.util.createHeaderAuthorization
import com.polzzak_android.data.remote.util.requestCatching
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userService: UserService
) {
    suspend fun requestUser(accessToken: String): ApiResult<UserResponse.UserResponseData> =
        requestCatching {
            val authorization = createHeaderAuthorization(accessToken = accessToken)
            userService.requestUserInfo(authorization = authorization)
        }
}