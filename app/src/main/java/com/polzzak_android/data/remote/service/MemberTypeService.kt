package com.polzzak_android.data.remote.service

import com.polzzak_android.data.remote.model.response.MemberTypeDetailsResponse
import retrofit2.Response
import retrofit2.http.GET

interface MemberTypeService {
    @GET("/api/v1/member-types")
    suspend fun requestMemberTypes(): Response<MemberTypeDetailsResponse>
}