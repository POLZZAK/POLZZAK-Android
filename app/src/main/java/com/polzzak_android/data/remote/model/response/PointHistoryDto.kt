package com.polzzak_android.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class PointHistoryDto(
    @SerializedName("startId")
    val startId: Int?,
    @SerializedName("content")
    val historyList: List<PointHistoryContentDto>
) {
    data class PointHistoryContentDto(
        @SerializedName("description")
        val description: String,
        @SerializedName("increasedPoint")
        val increasedPoint: Int,
        @SerializedName("remainingPoint")
        val remainingPoint: Int,
        @SerializedName("createdDate")
        val createdDate: String
    )
}
