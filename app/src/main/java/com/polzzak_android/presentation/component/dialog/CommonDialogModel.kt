package com.polzzak_android.presentation.component.dialog

import android.graphics.Bitmap
import android.text.Spannable
import com.polzzak_android.presentation.common.model.CommonButtonModel
import com.polzzak_android.presentation.feature.stamp.model.MissionModel
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
                require(content.stampImg == null) { "Stamp image must be null for ALERT type" }
                require(content.missionList == null) { "Mission list must be null for ALERT type" }
                require(content.captureBitmap == null) { "Capture view must be null for ALERT type" }
            }
            DialogStyleType.CALENDAR -> {
                require(content.calendar != null) { "Calendar must not be null for CALENDAR type" }
                require(content.mission == null) { "Mission must be null for CALENDAR type" }
                require(content.stampImg == null) { "Stamp image must be null for CALENDAR type" }
                require(content.missionList == null) { "Mission list must be null for CALENDAR type" }
                require(content.captureBitmap == null) { "Capture view must be null for CALENDAR type" }
            }
            DialogStyleType.MISSION -> {
                require(content.calendar == null) { "Calendar must be null for MISSION type" }
                require(content.mission != null) { "Mission must not be null for MISSION type" }
                require(content.stampImg == null) { "Stamp image must be null for MISSION type" }
                require(content.missionList == null) { "Mission list must be null for CALENDAR type" }
                require(content.captureBitmap == null) { "Capture view must be null for CALENDAR type" }
            }
            DialogStyleType.LOADING -> {
                require(content.calendar == null) { "Calendar must be null for LOADING type" }
                require(content.mission == null) { "Mission must be null for LOADING type" }
                require(content.stampImg == null) { "Stamp image must be null for LOADING type" }
                require(content.missionList == null) { "Mission list must be null for LOADING type" }
                require(content.captureBitmap == null) { "Capture view must be null for LOADING type" }
            }
            DialogStyleType.STAMP -> {
                require(content.calendar == null) { "Calendar must be null for STAMP type" }
                require(content.mission == null) { "Mission must be null for STAMP type" }
                require(content.stampImg != null) { "Stamp image not be null for STAMP type" }
                require(content.missionList == null) { "Mission list must be null for STAMP type" }
                require(content.captureBitmap == null) { "Capture view must be null for STAMP type" }
            }
            DialogStyleType.MISSION_LIST -> {
                require(content.calendar == null) { "Calendar must be null for MISSION LIST type" }
                require(content.mission == null) { "Mission must be null for MISSION LIST type" }
                require(content.stampImg == null) { "Stamp image must be null for MISSION LIST type" }
                require(content.missionList != null) { "Mission list not be null for MISSION LIST type" }
                require(content.captureBitmap == null) { "Capture view must be null for MISSION LIST type" }
            }
            DialogStyleType.CAPTURE -> {
                require(content.calendar == null) { "Calendar must be null for CAPTURE type" }
                require(content.mission == null) { "Mission must be null for CAPTURE type" }
                require(content.stampImg == null) { "Stamp image must be null for CAPTURE type" }
                require(content.missionList == null) { "Mission list must be null for CAPTURE type" }
                require(content.captureBitmap != null) { "Capture view must not be null for CAPTURE type" }
            }
        }
    }
}

data class CommonDialogContent(
    val title: Spannable,
    val body: Spannable? = null,
    val calendar: Calendar? = null,
    val mission: CommonDialogMissionData? = null,
    val stampImg: Int? = null,
    val missionList: List<MissionModel>? = null,
    val captureBitmap: Bitmap? = null
)

data class CommonDialogMissionData(
    val img: String,        // todo: 임시
    val missionTitle: String,
    val missionTime : String
)

enum class DialogStyleType {
    ALERT, CALENDAR, MISSION, LOADING, STAMP, MISSION_LIST, CAPTURE
}