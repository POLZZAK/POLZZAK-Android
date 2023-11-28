package com.polzzak_android.data.remote.model.response

import com.polzzak_android.presentation.feature.stamp.model.PartnerModel
import com.polzzak_android.presentation.feature.stamp.model.StampBoardModel
import com.polzzak_android.presentation.feature.stamp.model.StampBoardSummaryModel

data class MainStampBoardListResponse(
    override val code: Int?,
    override val messages: List<String>?,
    override val data: List<Data>?
): BaseResponse<List<MainStampBoardListResponse.Data>> {
    data class Data(
        val partner: PartnerDto?,                                // 연동된 사용자 정보
        val stampBoardSummaries: List<StampBoardSummaryDto>?     // 연동된 사용자와 공유하는 도장판 리스트
    )
}

fun MainStampBoardListResponse.Data.toStampBoardModel() = StampBoardModel(
    type = if (stampBoardSummaries.isNullOrEmpty()) 1 else 2,   // 도장판 없으면 1 있으면 2
    partner = partner?.toPartner(),
    stampBoardSummaries = stampBoardSummaries?.map {
        it.toStampBoardSummaryModel()
    }
)

data class PartnerDto(
    val memberId: Int,
    val memberType: MemberTypeDto,
    val nickname: String,
    val profileUrl: String
)

fun PartnerDto.toPartner() = PartnerModel(
    isKid = this.memberType.detail == "아이",
    memberId = this.memberId,
    memberType = this.memberType.detail,
    nickname = this.nickname,
    profileUrl = this.profileUrl
)

data class MemberTypeDto(
    val detail: String,
    val name: String
)

data class StampBoardSummaryDto(
    val currentStampCount: Int,         // 현재 도장 개수
    val goalStampCount: Int,            // 목표 도장 개수
    val missionRequestCount: Int,       // 요청 미션 개수
    val name: String,                   // 도장판 이름
    val reward: String,                 // 보상
    val stampBoardId: Int,              // 도장판 id
    val status: String                  // 도장판 상태 (completed, progress)
)

fun StampBoardSummaryDto.toStampBoardSummaryModel() = StampBoardSummaryModel(
    currentStampCount = this.currentStampCount,
    goalStampCount = this.goalStampCount,
    missionRequestCount = this.missionRequestCount,
    name = this.name,
    reward = this.reward,
    stampBoardId = this.stampBoardId
)