package com.polzzak_android.data.repository

import com.polzzak_android.data.remote.model.ApiResult
import com.polzzak_android.data.remote.model.request.LinkRequest
import com.polzzak_android.data.remote.model.response.FamiliesDto
import com.polzzak_android.data.remote.model.response.LinkRequestStatusResponse
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

    suspend fun requestLinkRequest(
        accessToken: String,
        targetId: Int
    ): ApiResult<Unit> = requestCatching {
        val authorization = createHeaderAuthorization(accessToken = accessToken)
        familyService.requestLinkRequest(
            authorization = authorization,
            linkRequest = LinkRequest(targetId = targetId)
        )
    }

    suspend fun requestApproveLinkRequest(
        accessToken: String,
        targetId: Int
    ): ApiResult<Unit> = requestCatching {
        val authorization = createHeaderAuthorization(accessToken = accessToken)
        familyService.requestApproveLinkRequest(
            authorization = authorization,
            id = targetId
        )
    }

    suspend fun requestRejectLinkRequest(
        accessToken: String,
        targetId: Int
    ): ApiResult<Unit> = requestCatching {
        val authorization = createHeaderAuthorization(accessToken = accessToken)
        familyService.requestRejectLinkRequest(
            authorization = authorization,
            id = targetId
        )
    }

    suspend fun requestCancelLinkRequest(
        accessToken: String,
        targetId: Int
    ): ApiResult<Unit> = requestCatching {
        val authorization = createHeaderAuthorization(accessToken = accessToken)
        familyService.requestCancelLinkRequest(
            authorization = authorization,
            id = targetId
        )
    }

    suspend fun requestDeleteLink(
        accessToken: String,
        targetId: Int
    ): ApiResult<Unit> = requestCatching {
        val authorization = createHeaderAuthorization(accessToken = accessToken)
        familyService.requestCancelLinkRequest(
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

    suspend fun requestSentRequestLinks(
        accessToken: String,
    ): ApiResult<FamiliesDto> = requestCatching {
        val authorization = createHeaderAuthorization(accessToken = accessToken)
        familyService.requestSentRequestLinks(authorization = authorization)
    }

    suspend fun requestReceivedRequestLinks(
        accessToken: String,
    ): ApiResult<FamiliesDto> = requestCatching {
        val authorization = createHeaderAuthorization(accessToken = accessToken)
        familyService.requestReceivedRequestLinks(authorization = authorization)
    }

    suspend fun requestIsRequestUpdated(
        accessToken: String
    ): ApiResult<LinkRequestStatusResponse.LinkRequestStatusDto> = requestCatching {
        val authorization = createHeaderAuthorization(accessToken = accessToken)
        familyService.requestIsRequestUpdated(authorization = authorization)
    }
}