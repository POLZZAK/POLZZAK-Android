package com.polzzak_android.data.remote.service

import com.polzzak_android.data.remote.model.request.MakeStampBoardRequest
import com.polzzak_android.data.remote.model.response.MainStampBoardListResponse
import com.polzzak_android.data.remote.model.response.MakeStampBoardResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
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
}