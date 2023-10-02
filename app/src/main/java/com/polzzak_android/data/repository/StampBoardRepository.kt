package com.polzzak_android.data.repository

import com.polzzak_android.data.remote.model.ApiResult
import com.polzzak_android.data.remote.model.request.MakeStampBoardRequest
import com.polzzak_android.data.remote.model.request.StampRequest
import com.polzzak_android.data.remote.model.response.EmptyDataResponse
import com.polzzak_android.data.remote.model.response.MainStampBoardListResponse
import com.polzzak_android.data.remote.model.response.StampBoardDetailDto
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
    ) : ApiResult<Nothing?>

    /**
     * 도장판 상세 데이터 조회
     */
    suspend fun getStampBoardDetailData(
        accessToken: String,
        stampBoardId: Int
    ): ApiResult<StampBoardDetailDto>

    /**
     * 보호자에게 도장 요청
     */
    suspend fun requestStampToProtector(
        accessToken: String,
        stampBoardId: Int,
        missionId: Int,
        guardianId: Int
    ): ApiResult<Unit>

    suspend fun receiveCoupon(
        accessToken: String,
        stampBoardId: Int
    ): ApiResult<Unit>

    /**
     * 도장 생성(도장 찍어주기) - 보호자
     */
    suspend fun makeStamp(
            accessToken: String,
            missionRequestId: Int,
            missionId: Int,
            stampDesignId: Int
    ): ApiResult<Unit>
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

    override suspend fun getStampBoardDetailData(
        accessToken: String,
        stampBoardId: Int
    ): ApiResult<StampBoardDetailDto> = requestCatching {
        val auth = createHeaderAuthorization(accessToken = accessToken)
        stampBoardService.getStampBoardDetailData(token = auth, stampBoardId = stampBoardId)
    }

    override suspend fun requestStampToProtector(
        accessToken: String,
        stampBoardId: Int,
        missionId: Int,
        guardianId: Int
    ): ApiResult<Unit> = requestCatching {
        val auth = createHeaderAuthorization(accessToken = accessToken)
        val requestData = StampRequest(
            stampBoardId = stampBoardId,
            missionId = missionId,
            guardianId = guardianId
        )

        stampBoardService.requestStampToProtector(token = auth, stampRequest = requestData)
    }

    override suspend fun receiveCoupon(
        accessToken: String,
        stampBoardId: Int
    ): ApiResult<Unit> = requestCatching {
        val auth = createHeaderAuthorization(accessToken = accessToken)

        stampBoardService.receiveCoupon(token = auth, stampBoardId = stampBoardId)
    }

    override suspend fun makeStamp(accessToken: String, missionRequestId: Int, missionId: Int, stampDesignId: Int): ApiResult<Unit> = requestCatching {
        val auth = createHeaderAuthorization(accessToken = accessToken)

        stampBoardService.makeStamp(token = auth, missionRequestId = missionRequestId, missionId = missionId, stampDesignId = stampDesignId)
    }
}