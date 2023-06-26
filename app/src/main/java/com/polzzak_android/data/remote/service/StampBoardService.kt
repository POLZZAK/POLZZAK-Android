package com.polzzak_android.data.remote.service

import com.polzzak_android.data.remote.model.request.MakeStampBoardRequest
import com.polzzak_android.data.remote.model.response.MakeStampBoardResponse
import com.polzzak_android.data.remote.model.response.StampBoardListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface StampBoardService {

    @GET("/api/v1/stamps/stamp-boards")
    suspend fun getMainStampBoards(
        @Query("stampBoardGroup") stampBoardGroup: String
    ) : Response<StampBoardListResponse>

    @POST("/api/v1/stamps/stamp-board")
    suspend fun makeStampBoard(
        @Header("Authorization") token: String,
        @Body stampBoardRequest: MakeStampBoardRequest
    ) : Response<MakeStampBoardResponse>
}