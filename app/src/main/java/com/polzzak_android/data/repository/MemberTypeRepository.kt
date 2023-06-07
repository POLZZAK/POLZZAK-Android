package com.polzzak_android.data.repository

import com.polzzak_android.data.remote.model.ApiResult
import com.polzzak_android.data.remote.model.response.MemberTypeDetailsResponse
import com.polzzak_android.data.remote.service.MemberTypeService
import com.polzzak_android.data.remote.util.requestCatching
import javax.inject.Inject

class MemberTypeRepository @Inject constructor(private val memberTypeService: MemberTypeService) {
    suspend fun requestMemberTypes(): ApiResult<MemberTypeDetailsResponse.MemberTypeDetailsResponseData> =
        requestCatching {
            memberTypeService.requestMemberTypes()
        }
}