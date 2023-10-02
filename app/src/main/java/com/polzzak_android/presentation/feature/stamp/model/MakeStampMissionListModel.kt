package com.polzzak_android.presentation.feature.stamp.model

data class MakeStampMissionListModel(
    val missionList: List<MakeStampMissionModel>,
    val isValidate: Boolean,
    val errorMessage: String?,
    val errorPosition: Int?
) {
    companion object {
        val init = MakeStampMissionListModel(
            missionList = listOf(MakeStampMissionModel(null, "")),
            isValidate = true,
            errorMessage = null,
            errorPosition = null
        )
    }
}

data class MakeStampMissionModel(
        val id: Int? = null,
        val content: String
)
