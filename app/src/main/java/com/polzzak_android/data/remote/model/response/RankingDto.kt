package com.polzzak_android.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class RankingDto(
    @SerializedName("memberSimpleResponse")
    val currentUserRanking: CurrentUserRankingDto,
    val rankingList: List<UserRankingDto>
) {
    data class CurrentUserRankingDto(
        @SerializedName("nickname")
        val nickname: String,
        @SerializedName("memberPoint")
        val memberPoint: MemberPoint,
        @SerializedName("memberType")
        val memberType: MemberTypeInfo,
        @SerializedName("profileUrl")
        val profileUrl: String,
        @SerializedName("myRanking")
        val ranking: Int

    ) {
        data class MemberPoint(
            @SerializedName("point")
            val point: Int,
            @SerializedName("level")
            val level: Int
        )

        data class MemberTypeInfo(
            @SerializedName("name")
            val name: String,
            @SerializedName("detail")
            val detail: String
        )
    }

    data class UserRankingDto(
        val ranking: Int,
        val rankingStatus: String,
        val nickname: String,
        val point: Int,
        val level: Int,
        val memberTypeDetail: String?,
        val profileUrl: String,
        val isMe: Boolean
    )
}