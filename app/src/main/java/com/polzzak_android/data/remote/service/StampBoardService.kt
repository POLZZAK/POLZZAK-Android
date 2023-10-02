package com.polzzak_android.data.remote.service

import com.polzzak_android.data.remote.model.request.MakeStampBoardRequest
import com.polzzak_android.data.remote.model.request.StampRequest
import com.polzzak_android.data.remote.model.response.EmptyDataResponse
import com.polzzak_android.data.remote.model.response.MainStampBoardListResponse
import com.polzzak_android.data.remote.model.response.MakeStampBoardResponse
import com.polzzak_android.data.remote.model.response.StampBoardDetailResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface StampBoardService {
    @GET("/api/v1/stamps/stamp-boards")
    suspend fun getMainStampBoards(
        @Header("Authorization") authorization: String,
        @Query("partnerMemberId") linkedMemberId: String?,     // 조회할 member ID
        @Query("stampBoardGroup") stampBoardGroup: String           // 진행 중인 도장판 여부(in_progress, ended)
    ) : Response<MainStampBoardListResponse>

    @POST("/api/v1/stamps/stamp-boards")
    suspend fun makeStampBoard(
        @Header("Authorization") token: String,
        @Body stampBoardRequest: MakeStampBoardRequest
    ) : Response<MakeStampBoardResponse>

    @GET("/api/v1/stamps/stamp-boards/{stampBoardId}")
    suspend fun getStampBoardDetailData(
        @Header("Authorization") token: String,
        @Path("stampBoardId") stampBoardId: Int
    ): Response<StampBoardDetailResponse>

    /**
     * 보호자에게 도장 요청 API
     */
    @POST("/api/v1/stamps/mission-requests")
    suspend fun requestStampToProtector(
        @Header("Authorization") token: String,
        @Body stampRequest: StampRequest
    ): Response<EmptyDataResponse>

    /**
     * 쿠폰 수령 API(아이)
     */
    @POST("/api/v1/coupons")
    suspend fun receiveCoupon(
        @Header("Authorization") token: String,
        @Field("stampBoardId") stampBoardId: Int
    ): Response<EmptyDataResponse>

    /**
     * 도장 생성(도장 찍어주기) - 보호자
     */
    @POST("/api/v1/stamps/stamp-boards")
    suspend fun makeStamp(
            @Header("Authorization") token: String,
            @Field("missionRequestId") missionRequestId: Int,
            @Field("missionId") missionId: Int,
            @Field("stampDesignId") stampDesignId: Int,
    ): Response<EmptyDataResponse>
}