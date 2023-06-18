package com.polzzak_android.presentation.common.model

import java.util.Calendar

data class CommonDialogModel(
    val type: DialogStyleType,
    val content: CommonDialogContent,
    val button: CommonButtonModel
) {
    // todo: 린트 적용 전 임시
    init {
        when (type) {
            DialogStyleType.ALERT -> {
                require(content.calendar == null) { "Calendar must be null for ALERT type" }
                require(content.mission == null) { "Mission must be null for ALERT type" }
            }
            DialogStyleType.CALENDAR -> {
                require(content.calendar != null) { "Calendar must not be null for CALENDAR type" }
                require(content.mission == null) { "Mission must be null for CALENDAR type" }
            }
            DialogStyleType.MISSION -> {
                require(content.calendar == null) { "Calendar must be null for MISSION type" }
                require(content.mission != null) { "Mission must not be null for MISSION type" }
            }
            DialogStyleType.LOADING -> {
                require(content.calendar == null) { "Calendar must be null for LOADING type" }
                require(content.mission == null) { "Mission must be null for LOADING type" }
            }
        }
    }
}

data class CommonDialogContent(
    val title: String,
    val body: String? = null,
    val calendar: Calendar? = null,
    val mission: CommonDialogMissionData? = null
)

data class CommonDialogMissionData(
    val img: String,        // todo: 임시
    val missionTitle: String,
    val missionTime : String
)

enum class DialogStyleType {
    ALERT, CALENDAR, MISSION, LOADING
}