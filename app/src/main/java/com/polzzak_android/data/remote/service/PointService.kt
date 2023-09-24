package com.polzzak_android.data.remote.service

import com.polzzak_android.data.remote.model.response.RankingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface PointService {
    /**
     * 보호자 전용 랭킹 리스트 API
     */
    @GET("api/v1/rankings/{type}")
    suspend fun getRankingList(
        @Header("Authorization") authorization: String,
        @Path("type") type: String
    ): Response<RankingResponse>

    /**
     * 아이 전용 랭킹 리스트 API
     */
    @GET("api/v1/rankings/kids")
    suspend fun getKidRanking(
        @Header("Authorization") authorization: String,
    ): Response<RankingResponse>
}