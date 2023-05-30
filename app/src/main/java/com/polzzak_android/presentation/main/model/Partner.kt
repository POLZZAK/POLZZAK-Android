package com.polzzak_android.presentation.main.model

import com.polzzak_android.data.remote.model.response.StampBoardListResponse

data class Partner(
    val memberId: Int,
    val memberType: String,
    val nickname: String,
    val profileUrl: String
)

fun convertToPartner(partnerData: StampBoardListResponse.StampBoardListResponseData.PartnerData): Partner {
    return Partner(
        memberId = partnerData.memberId,
        memberType = partnerData.memberType,
        nickname = partnerData.nickname,
        profileUrl = partnerData.profileUrl
    )
}