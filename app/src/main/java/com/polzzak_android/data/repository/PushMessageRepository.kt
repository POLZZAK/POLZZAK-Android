package com.polzzak_android.data.repository

import com.google.firebase.messaging.FirebaseMessaging
import com.polzzak_android.data.remote.model.ApiException
import com.polzzak_android.data.remote.model.ApiResult
import com.polzzak_android.data.remote.model.request.PushTokenRequest
import com.polzzak_android.data.remote.service.PushMessageService
import com.polzzak_android.data.remote.util.createHeaderAuthorization
import com.polzzak_android.data.remote.util.requestCatching
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class PushMessageRepository @Inject constructor(private val pushMessageService: PushMessageService) {
    suspend fun requestPostPushToken(accessToken: String, token: String) = requestCatching {
        val authorization = createHeaderAuthorization(accessToken = accessToken)
        pushMessageService.requestPostPushToken(
            authorization = authorization,
            pushTokenRequest = PushTokenRequest(token = token)
        )
    }

    suspend fun requestPushToken(): ApiResult<String> {
        return try {
            val tokenTask = FirebaseMessaging.getInstance().token
            val token = tokenTask.await()
            if (tokenTask.isSuccessful) {
                ApiResult.Success(token)
            } else {
                ApiResult.Error(ApiException.FirebaseTokenFailed())
            }
        } catch (e: Exception) {
            ApiResult.Error(e)
        }
    }
}