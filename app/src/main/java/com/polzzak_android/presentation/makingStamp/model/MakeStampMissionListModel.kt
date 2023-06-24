package com.polzzak_android.presentation.makingStamp.model

data class MakeStampMissionListModel(
    val missionList: List<String>,
    val isValidate: Boolean,
    val errorMessage: String?,
    val errorPosition: Int?
) {
    companion object {
        val init = MakeStampMissionListModel(
            missionList = listOf("test1", "test2"),
            isValidate = true,
            errorMessage = null,
            errorPosition = null
        )
    }
}
