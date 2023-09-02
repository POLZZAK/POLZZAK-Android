package com.polzzak_android.data.repository

import com.polzzak_android.data.remote.model.ApiResult
import com.polzzak_android.data.remote.model.response.CouponListResponse
import com.polzzak_android.data.remote.model.response.StampBoardListResponse
import com.polzzak_android.data.remote.service.CouponService
import com.polzzak_android.data.remote.util.createHeaderAuthorization
import com.polzzak_android.data.remote.util.requestCatching
import retrofit2.Response
import javax.inject.Inject

interface CouponRepository {
    suspend fun getCouponList(
        token: String,
        couponState: String,
        partnerMemberId: Int?
    ): ApiResult<List<CouponListResponse.Data>>
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
}