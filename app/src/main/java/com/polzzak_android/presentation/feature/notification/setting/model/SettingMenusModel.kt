package com.polzzak_android.presentation.feature.notification.setting.model

data class SettingMenusModel(
    val typeToCheckedMap: Map<SettingMenuType, Boolean> = LinkedHashMap(),
    val isAllMenuChecked: Boolean = false
)

fun asSettingMenusModel(settings: Map<String, Boolean?>): SettingMenusModel {
    val typeToCheckedMap = LinkedHashMap<SettingMenuType, Boolean>()
    SettingMenuType.values().forEach { type ->
        settings[type.dataString]?.let { isChecked ->
            typeToCheckedMap[type] = isChecked
        }
    }
    return SettingMenusModel(
        typeToCheckedMap = typeToCheckedMap,
        isAllMenuChecked = typeToCheckedMap.any { it.value })
}