package com.polzzak_android.presentation.feature.notification.setting.model

data class SettingMenusModel(
    val typeToCheckedMap: HashMap<SettingMenuType, Boolean> = hashMapOf(),
    val isAllMenuChecked: Boolean = false
)
