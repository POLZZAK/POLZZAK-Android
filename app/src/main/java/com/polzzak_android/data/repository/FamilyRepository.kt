package com.polzzak_android.data.repository

import com.polzzak_android.data.remote.model.ApiResult
import com.polzzak_android.data.remote.model.request.LinkRequest
import com.polzzak_android.data.remote.model.response.FamiliesDto
import com.polzzak_android.data.remote.model.response.UserInfoDto
import com.polzzak_android.data.remote.service.FamilyService
import com.polzzak_android.data.remote.util.createHeaderAuthorization
import com.polzzak_android.data.remote.util.requestCatching
import javax.inject.Inject

class FamilyRepository @Inject constructor(
    private val familyService: FamilyService
) {

    suspend fun requestUserWithNickName(
        accessToken: String,
        nickName: String
    ): ApiResult<UserInfoDto> = requestCatching {
        val authorization = createHeaderAuthorization(accessToken = accessToken)
        familyService.requestUserWithNickName(authorization = authorization, nickName = nickName)
    }

    suspend fun requestLink(
        accessToken: String,
        targetId: Int
    ): ApiResult<Nothing?> = requestCatching {
        val authorization = createHeaderAuthorization(accessToken = accessToken)
        familyService.requestLink(
            authorization = authorization,
            linkRequest = LinkRequest(targetId = targetId)
        )
    }

    suspend fun requestApproveLink(
        accessToken: String,
        targetId: Int
    ): ApiResult<Nothing?> = requestCatching {
        val authorization = createHeaderAuthorization(accessToken = accessToken)
        familyService.requestApproveLink(
            authorization = authorization,
            id = targetId
        )
    }

    suspend fun requestRejectLink(
        accessToken: String,
        targetId: Int
    ): ApiResult<Nothing?> = requestCatching {
        val authorization = createHeaderAuthorization(accessToken = accessToken)
        familyService.requestRejectLink(
            authorization = authorization,
            id = targetId
        )
    }

    suspend fun requestDeleteLink(
        accessToken: String,
        targetId: Int
    ): ApiResult<Nothing?> = requestCatching {
        val authorization = createHeaderAuthorization(accessToken = accessToken)
        familyService.requestDeleteLink(
            authorization = authorization,
            id = targetId
        )
    }

    suspend fun requestLinkedUsers(
        accessToken: String,
    ): ApiResult<FamiliesDto> = requestCatching {
        val authorization = createHeaderAuthorization(accessToken = accessToken)
        familyService.requestLinkedUsers(authorization = authorization)
    }

    suspend fun requestSentRequestLinkUsers(
        accessToken: String,
    ): ApiResult<FamiliesDto> = requestCatching {
        val authorization = createHeaderAuthorization(accessToken = accessToken)
        familyService.requestSentRequestLinkUsers(authorization = authorization)
    }

    suspend fun requestReceivedRequestLinkUsers(
        accessToken: String,
    ): ApiResult<FamiliesDto> = requestCatching {
        val authorization = createHeaderAuthorization(accessToken = accessToken)
        familyService.requestReceivedRequestLinkUsers(authorization = authorization)
    }
}