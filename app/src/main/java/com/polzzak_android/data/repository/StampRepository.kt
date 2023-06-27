package com.polzzak_android.data.repository

import com.polzzak_android.data.remote.model.ApiResult
import com.polzzak_android.data.remote.model.StampBoardGroup
import com.polzzak_android.data.remote.model.response.MissionDto
import com.polzzak_android.data.remote.model.response.MissionRequestDto
import com.polzzak_android.data.remote.model.response.StampBoardDetailDto
import com.polzzak_android.data.remote.model.response.StampBoardDetailResponse
import com.polzzak_android.data.remote.model.response.StampBoardListResponse
import com.polzzak_android.data.remote.model.response.StampDto

interface StampRepository {
    /**
     * 도장판 목록 리스트 요청
     */
    suspend fun getStampBoardList(
        stampBoardGroup: StampBoardGroup,
        memberId: String = ""
    ): ApiResult<List<StampBoardListResponse.StampBoardListResponseData>>

    suspend fun getStampBoardDetail(stampBoardId: Int): ApiResult<StampBoardDetailDto>
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

    override suspend fun getStampBoardDetail(stampBoardId: Int): ApiResult<StampBoardDetailDto> {
        val fakeData = StampBoardDetailDto(
            stampBoardId = 33,
            name = "테스트 도장판",
            status = "progress",
            currentStampCount = 13,
            goalStampCount = 30,
            reward = "칭찬해주기",
            missions = listOf(
                MissionDto(id = 1, content = "미션1"),
                MissionDto(id = 2, content = "미션2"),
                MissionDto(id = 2, content = "미션3"),
                MissionDto(id = 2, content = "미션4")
            ),
            stamps = listOf(
                StampDto(id = 11, stampDesignId = 1, missionContent = "미션1", createdDate = "2023-06-12T15:31:12.213331766"),
                StampDto(id = 12, stampDesignId = 3, missionContent = "미션2", createdDate = "2023-06-12T15:31:12.213357066")
            ),
            missionRequestList = listOf(
                MissionRequestDto(id = 21, missionContent = "미션3", createdDate = "2023-06-12T15:31:12.213646468"),
                MissionRequestDto(id = 22, missionContent = "미션4", createdDate = "2023-06-12T15:31:12.213668468")
            ),
            completedDate = null,
            rewardDate = null,
            createdDate = "2023-06-12T15:31:12.213769168"
        )


        return ApiResult.success(fakeData)
    }
}