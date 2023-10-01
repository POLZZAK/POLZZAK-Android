package com.polzzak_android.data.repository

import com.polzzak_android.data.remote.model.ApiResult
import com.polzzak_android.data.remote.model.response.PointHistoryDto
import com.polzzak_android.data.remote.model.response.RankingDto
import com.polzzak_android.data.remote.service.PointService
import com.polzzak_android.data.remote.util.createHeaderAuthorization
import com.polzzak_android.data.remote.util.requestCatching
import javax.inject.Inject

interface PointRepository {
    suspend fun getProtectorRankingList(
        token: String
    ): ApiResult<RankingDto>

    suspend fun getKidRankingList(
        token: String
    ): ApiResult<RankingDto>

    /**
     * 포인트 내역 조회
     */
    suspend fun getPointHistoryList(
        token: String,
        startId: Int
    ): ApiResult<PointHistoryDto>
}

class PointRepositoryImpl @Inject constructor(
    private val pointService: PointService
) : PointRepository {
    private companion object {
        const val RANKING_PROTECTOR = "guardians"
        const val RANKING_KID = "kids"
    }

    override suspend fun getProtectorRankingList(token: String): ApiResult<RankingDto> = requestCatching {
        val accessToken = createHeaderAuthorization(token)
        pointService.getRankingList(authorization = accessToken, type = RANKING_PROTECTOR)
    }

    override suspend fun getKidRankingList(token: String): ApiResult<RankingDto> = requestCatching {
        val accessToken = createHeaderAuthorization(token)
        pointService.getRankingList(authorization = accessToken, type = RANKING_KID)
    }

    override suspend fun getPointHistoryList(
        token: String,
        startId: Int
    ): ApiResult<PointHistoryDto> = requestCatching {
        val accessToken = createHeaderAuthorization(token)
        pointService.getPointHistoryList(authorization = accessToken, startId = startId)
    }
}