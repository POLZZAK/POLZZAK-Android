package com.polzzak_android.data.repository

import com.polzzak_android.data.remote.model.response.MemberTypeDetailsResponse
import com.polzzak_android.data.remote.service.MemberTypeService
import retrofit2.Response
import javax.inject.Inject

class MemberTypeRepository @Inject constructor(private val memberTypeService: MemberTypeService) {
    suspend fun requestMemberTypes(): Response<MemberTypeDetailsResponse> =
        memberTypeService.requestMemberTypes()
}