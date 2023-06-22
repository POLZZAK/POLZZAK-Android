package com.polzzak_android.data.repository

import com.polzzak_android.data.remote.model.ApiResult
import com.polzzak_android.data.remote.model.request.MakeStampBoardRequest
import com.polzzak_android.data.remote.model.response.StampBoardListResponse
import com.polzzak_android.data.remote.service.StampBoardService
import com.polzzak_android.data.remote.util.requestCatching
import retrofit2.Response
import javax.inject.Inject

class StampBoardRepository @Inject constructor(
    private val stampBoardService: StampBoardService
) {

    suspend fun getStampBoard(
        stampBoardGroup: String
    ): Response<StampBoardListResponse> {
        return stampBoardService.getMainStampBoards(stampBoardGroup = stampBoardGroup)
    }

    suspend fun makeStampBoard(
        token: String,
        newStampBoard: MakeStampBoardRequest
    ) : ApiResult<Nothing?> = requestCatching {
        stampBoardService.makeStampBoard(token = token, stampBoardRequest = newStampBoard)
    }
}