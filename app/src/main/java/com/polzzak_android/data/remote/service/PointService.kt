package com.polzzak_android.data.remote.service

import com.polzzak_android.data.remote.model.response.PointHistoryResponse
import com.polzzak_android.data.remote.model.response.RankingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface PointService {
    /**
     * 랭킹 리스트 API
     *
     * @param type 'kids' or 'guardians'
     */
    @GET("api/v1/rankings/{type}")
    suspend fun getRankingList(
        @Header("Authorization") authorization: String,
        @Path("type") type: String
    ): Response<RankingResponse>

    /**
     * 포인트 내역 조회 API
     */
    @GET("api/v1/member-points/earning-histories/me")
    suspend fun getPointHistoryList(
        @Header("Authorization") authorization: String,
        @Query("startId") startId: Int
    ): Response<PointHistoryResponse>
}