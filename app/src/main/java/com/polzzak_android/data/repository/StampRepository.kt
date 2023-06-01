package com.polzzak_android.data.repository

import com.polzzak_android.presentation.common.model.ApiResult
import com.polzzak_android.data.remote.model.StampBoardGroup
import com.polzzak_android.data.remote.model.response.StampBoardListResponse

interface StampRepository {
    /**
     * 도장판 목록 리스트 요청
     */
    suspend fun getStampBoardList(
        stampBoardGroup: StampBoardGroup,
        memberId: String = ""
    ): ApiResult<List<StampBoardListResponse.StampBoardListResponseData>>
}

/**
 * fake data 받기 위한 구현클래스
 */
class MockStampRepositoryImpl : StampRepository {
    override suspend fun getStampBoardList(
        stampBoardGroup: StampBoardGroup,
        memberId: String
    ): ApiResult<List<StampBoardListResponse.StampBoardListResponseData>> {
        val fakeData = listOf(
            StampBoardListResponse.StampBoardListResponseData(
                partner = StampBoardListResponse.StampBoardListResponseData.PartnerData(
                    memberId = 11,
                    nickname = "아이 1",
                    memberType = "KID",
                    profileUrl = "url"
                ),
                stampBoardSummaries = listOf(
                    StampBoardListResponse.StampBoardListResponseData.StampBoardSummaryData(
                        stampBoardId = 1,
                        name = "도장판 1",
                        currentStampCount = 13,
                        goalStampCount = 30,
                        reward = "콘서트",
                        missionRequestCount = 5,
                        status = "progress"
                    ),
                    StampBoardListResponse.StampBoardListResponseData.StampBoardSummaryData(
                        stampBoardId = 3,
                        name = "도장판 3",
                        currentStampCount = 30,
                        goalStampCount = 30,
                        reward = "칭찬",
                        missionRequestCount = 0,
                        status = "completed"
                    )
                )
            ),
            StampBoardListResponse.StampBoardListResponseData(
                partner = StampBoardListResponse.StampBoardListResponseData.PartnerData(
                    memberId = 12,
                    nickname = "아이 2",
                    memberType = "KID",
                    profileUrl = "image_url"
                ),
                stampBoardSummaries = emptyList()
            )
        )

        return ApiResult.success(fakeData)
    }
}