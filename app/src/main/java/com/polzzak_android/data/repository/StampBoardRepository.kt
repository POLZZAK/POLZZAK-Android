package com.polzzak_android.data.repository

import com.polzzak_android.data.remote.model.ApiResult
import com.polzzak_android.data.remote.model.request.MakeStampBoardRequest
import com.polzzak_android.data.remote.model.response.MainStampBoardListResponse
import com.polzzak_android.data.remote.service.StampBoardService
import com.polzzak_android.data.remote.util.createHeaderAuthorization
import com.polzzak_android.data.remote.util.requestCatching
import javax.inject.Inject

interface StampBoardRepository {
    suspend fun getStampBoardList(
        accessToken: String,
        linkedMemberId: String?,
        stampBoardGroup: String
    ): ApiResult<List<MainStampBoardListResponse.Data>>

    suspend fun makeStampBoard(
        accessToken: String,
        newStampBoard: MakeStampBoardRequest
    ): ApiResult<Nothing?>
}

class StampBoardRepositoryImpl @Inject constructor(
    private val stampBoardService: StampBoardService
): StampBoardRepository {
    /**
     * 도장판 목록 조회
     */
    override suspend fun getStampBoardList(
        accessToken: String,
        linkedMemberId: String?,
        stampBoardGroup: String
    ): ApiResult<List<MainStampBoardListResponse.Data>> = requestCatching {
        val authorization = createHeaderAuthorization(accessToken = accessToken)
        stampBoardService.getMainStampBoards(authorization = authorization, linkedMemberId, stampBoardGroup)
    }

    /**
     * 도장판 생성
     */
    override suspend fun makeStampBoard(
        accessToken: String,
        newStampBoard: MakeStampBoardRequest
    ) : ApiResult<Nothing?> = requestCatching {
        val authorization = createHeaderAuthorization(accessToken = accessToken)
        stampBoardService.makeStampBoard(token = authorization, stampBoardRequest = newStampBoard)
    }
}