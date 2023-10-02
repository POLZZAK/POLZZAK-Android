package com.polzzak_android.presentation.feature.stamp.make.intreraction

import android.widget.ImageButton
import com.polzzak_android.presentation.feature.stamp.model.MakeStampMissionModel

interface MissionInteraction {
    fun onDeletedMissionIconClicked(mission: String, view: ImageButton)
    fun updateMissionList(missionList: List<MakeStampMissionModel>)
}