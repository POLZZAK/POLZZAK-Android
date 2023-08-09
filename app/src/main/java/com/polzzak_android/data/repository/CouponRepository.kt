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
    ): ApiResult<CouponDetailDto> {
        val fake = CouponDetailDto(
            couponId = 1,
            reward = "상상상",
            guardian = CouponDetailDto.ProfileDto(
                nickname = "보호자",
                profileUrl = "profile url"
            ),
            kid = CouponDetailDto.ProfileDto(
                nickname = "아이",
                profileUrl = "profile url"
            ),
            missionContents = listOf("미션1", "미션2", "미션3"),
            stampCount = 10,
            state = "ISSUED",
            rewardRequestDate = "2023-07-31T13:59:43.659572713",
            startDate = "2023-07-31T15:59:43.659589415",
            endDate = "2023-07-31T15:59:43.659595016"
        )

        return ApiResult.success(fake)
    }
}