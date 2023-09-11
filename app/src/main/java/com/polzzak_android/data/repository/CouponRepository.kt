package com.polzzak_android.data.repository

import com.polzzak_android.data.remote.model.ApiResult
import com.polzzak_android.data.remote.model.response.CouponListResponse
import com.polzzak_android.data.remote.service.CouponService
import com.polzzak_android.data.remote.util.createHeaderAuthorization
import com.polzzak_android.data.remote.util.requestCatching
import com.polzzak_android.data.remote.model.response.CouponDetailDto
import javax.inject.Inject

interface CouponRepository {
    suspend fun getCouponList(
        token: String,
        couponState: String,
        partnerMemberId: Int?
    ): ApiResult<List<CouponListResponse.Data>>

    suspend fun getCouponDetail(
        token: String,
        couponId: Int
    ): ApiResult<CouponDetailDto>
}


class CouponRepositoryImpl @Inject constructor(
    private val couponService: CouponService
): CouponRepository {

    override suspend fun getCouponList(
        token: String,
        couponState: String,
        partnerMemberId: Int?
    ): ApiResult<List<CouponListResponse.Data>> = requestCatching {
        val accessToken = createHeaderAuthorization(token)
        couponService.getCouponList(accessToken, couponState, partnerMemberId)
    }

    override suspend fun getCouponDetail(
        token: String,
        couponId: Int
    ): ApiResult<CouponDetailDto> = requestCatching {
        val accessToken = createHeaderAuthorization(token)
        couponService.getCouponDetail(token, couponId)
    }
}