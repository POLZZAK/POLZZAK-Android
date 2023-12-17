package com.polzzak_android.data.repository

import com.polzzak_android.data.remote.model.ApiResult
import com.polzzak_android.data.remote.model.request.IssueCouponRequest
import com.polzzak_android.data.remote.model.request.MakeStampBoardRequest
import com.polzzak_android.data.remote.model.request.MakeStampRequest
import com.polzzak_android.data.remote.model.request.ReceiveCouponRequest
import com.polzzak_android.data.remote.model.request.StampRequest
import com.polzzak_android.data.remote.model.request.UpdateStampBoardRequest
import com.polzzak_android.data.remote.model.response.MainStampBoardListResponse
import com.polzzak_android.data.remote.model.response.StampBoardDetailDto
import com.polzzak_android.data.remote.service.StampBoardService
import com.polzzak_android.data.remote.util.createHeaderAuthorization
import com.polzzak_android.data.remote.util.requestCatching
import java.time.LocalDate
import java.time.ZoneOffset
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

    suspend fun updateStampBoard(
            accessToken: String,
            stampBoardId: Int,
            request: UpdateStampBoardRequest
    ) : ApiResult<StampBoardDetailDto>

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
        stampBoardId: Int,
        stampDesignId: Int,
        missionId: Int?,
        missionRequestId: Int?,
    ): ApiResult<Unit>

    /**
     * 도장 요청 거절 - 보호자
     */
    suspend fun rejectMissionRequest(
        accessToken: String,
        missionRequestId: Int
    ): ApiResult<Unit>

    /**
     * 쿠폰 발급 - 보호자
     */
    suspend fun issueCoupon(
        accessToken: String,
        stampBoardId: Int,
        rewardDate: LocalDate
    ): ApiResult<Unit>

    /**
     * 도장판 삭제 - 보호자
     */
    suspend fun deleteStampBoard(
        accessToken: String,
        stampBoardId: Int
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

    /**
     * 도장판 수정
     */
    override suspend fun updateStampBoard(accessToken: String, stampBoardId: Int, request: UpdateStampBoardRequest): ApiResult<StampBoardDetailDto> = requestCatching {
        val authorization = createHeaderAuthorization(accessToken = accessToken)
        stampBoardService.updateStampBoard(token = authorization, stampBoardId = stampBoardId, request = request)
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
        val request = ReceiveCouponRequest(stampBoardId = stampBoardId)
        stampBoardService.receiveCoupon(token = auth, receiveCouponRequest = request)
    }

    override suspend fun makeStamp(
        accessToken: String,
        stampBoardId: Int,
        stampDesignId: Int,
        missionId: Int?,
        missionRequestId: Int?,
    ): ApiResult<Unit> = requestCatching {
        val auth = createHeaderAuthorization(accessToken = accessToken)

        stampBoardService.makeStamp(
            token = auth,
            stampBoardId = stampBoardId,
            request = MakeStampRequest(
                missionId = missionId,
                missionRequestId = missionRequestId,
                stampDesignId = stampDesignId
            )
        )
    }

    override suspend fun rejectMissionRequest(
        accessToken: String,
        missionRequestId: Int
    ): ApiResult<Unit> = requestCatching {
        val auth = createHeaderAuthorization(accessToken = accessToken)

        stampBoardService.rejectMissionRequest(
            token = auth,
            missionRequestId = missionRequestId
        )
    }

    override suspend fun issueCoupon(
        accessToken: String,
        stampBoardId: Int,
        rewardDate: LocalDate
    ): ApiResult<Unit> = requestCatching {
        val auth = createHeaderAuthorization(accessToken = accessToken)

        stampBoardService.issueCoupon(
            token = auth,
            stampBoardId = stampBoardId,
            request = IssueCouponRequest(
                rewardDate = rewardDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
            )
        )
    }

    override suspend fun deleteStampBoard(
        accessToken: String,
        stampBoardId: Int
    ): ApiResult<Unit> = requestCatching {
        val auth = createHeaderAuthorization(accessToken = accessToken)

        stampBoardService.deleteStampBoard(
            token = auth,
            stampBoardId = stampBoardId
        )
    }
}