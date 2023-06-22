package com.polzzak_android.presentation.makingStamp.intreraction

import android.widget.ImageButton

interface MissionInteraction {
    fun onDeletedMissionIconClicked(mission: String, view: ImageButton)
    fun updateMissionList(missionList: List<String>)
}