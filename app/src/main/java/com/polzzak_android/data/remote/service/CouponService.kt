package com.polzzak_android.data.remote.service

import com.polzzak_android.data.remote.model.response.CouponDetailResponse
import com.polzzak_android.data.remote.model.response.CouponListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface CouponService {

    @GET("/api/v1/coupons")
    suspend fun getCouponList(
        @Header("Authorization") token: String,
        @Query("couponState") couponState: String,
        @Query("partnerMemberId") partnerMemberId: Int?
    ) : Response<CouponListResponse>

    @GET("/api/v1/coupons/{couponId}")
    suspend fun getCouponDetail(
        @Header("Authorization") token: String,
        @Path("couponId") couponId: Int
    ): Response<CouponDetailResponse>
}