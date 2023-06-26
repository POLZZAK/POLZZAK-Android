package com.polzzak_android.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class MakeStampBoardRequest(
    val kidId: Int,
    @SerializedName("name")
    val stampBoardName: String,
    @SerializedName("goalStampCount")
    val stampBoardCount: Int,
    @SerializedName("reward")
    val stampBoardReward: String,
    @SerializedName("missionContents")
    val missionList: List<String>
)