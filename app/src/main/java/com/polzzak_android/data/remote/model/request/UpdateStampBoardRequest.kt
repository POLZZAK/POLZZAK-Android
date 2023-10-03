package com.polzzak_android.data.remote.model.request

import com.polzzak_android.presentation.feature.stamp.model.MakeStampMissionModel

data class UpdateStampBoardRequest(
    val goalStampCount: Int,
    val missions: List<MakeStampMissionModel>,
    val name: String,
    val reward: String
)