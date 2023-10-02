package com.polzzak_android.presentation.feature.stamp.model

data class MakeStampMissionListModel(
    val missionList: List<String>,
    val isValidate: Boolean,
    val errorMessage: String?,
    val errorPosition: Int?
) {
    companion object {
        val init = MakeStampMissionListModel(
            missionList = listOf("", ""),
            isValidate = true,
            errorMessage = null,
            errorPosition = null
        )
    }
}
