package com.polzzak_android.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class StampBoardDetailDto(
    @SerializedName("stampBoardId")
    val stampBoardId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("currentStampCount")
    val currentStampCount: Int,
    @SerializedName("goalStampCount")
    val goalStampCount: Int,
    @SerializedName("reward")
    val reward: String,
    @SerializedName("missions")
    val missions: List<MissionDto>,
    @SerializedName("stamps")
    val stamps: List<StampDto>,
    @SerializedName("missionRequestList")
    val missionRequestList: List<MissionRequestDto>,
    @SerializedName("completedDate")
    val completedDate: String?,
    @SerializedName("rewardDate")
    val rewardDate: String?,
    @SerializedName("createdDate")
    val createdDate: String
)