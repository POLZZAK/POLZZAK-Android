package com.polzzak_android.data.remote.model.response

data class StampBoardListResponse(
    override val code: Int?,
    override val messages: List<String>?,
    override val data: StampBoardListResponseData?,
) : BaseResponse<StampBoardListResponse.StampBoardListResponseData> {

    data class StampBoardListResponseData(
        val partner: PartnerData,
        val stampBoardSummaries: List<StampBoardSummaryData>
    ) {
        data class PartnerData(
            val memberId: Int,
            val nickname: String,
            val memberType: String,
            val profileUrl: String
        )

        data class StampBoardSummaryData(
            val stampBoardId: Int,
            val name: String,
            val currentStampCount: Int,
            val goalStampCount: Int,
            val reward: String,
            val missionRequestCount: Int,
            val status: String
        )
    }
}
