package com.polzzak_android.presentation.feature.notification.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polzzak_android.common.util.livedata.EventWrapper
import com.polzzak_android.data.repository.NotificationRepository
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.model.copyWithData
import com.polzzak_android.presentation.feature.notification.setting.model.SettingMenuType
import com.polzzak_android.presentation.feature.notification.setting.model.SettingMenusModel
import com.polzzak_android.presentation.feature.notification.setting.model.SettingModel
import com.polzzak_android.presentation.feature.notification.setting.model.asSettingMenusModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationSettingViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {
    private val _errorEventLiveData = MutableLiveData<EventWrapper<Exception>>()
    val errorEventLiveData: LiveData<EventWrapper<Exception>> = _errorEventLiveData

    private val _settingMenuLiveData =
        MutableLiveData<ModelState<SettingMenusModel>>()
    private var requestSettingMenusJob: Job? = null

    private val _isGrantedPermissionLiveData = MutableLiveData<Boolean?>(null)

    val settingMenusLiveData = MediatorLiveData<ModelState<SettingModel>>().apply {
        addSource(_settingMenuLiveData) {
            value = it.copyWithData(
                newData = SettingModel(
                    menusModel = it.data,
                    isGranted = _isGrantedPermissionLiveData.value
                )
            )
        }
        addSource(_isGrantedPermissionLiveData) {
            value = (_settingMenuLiveData.value
                ?: ModelState.Success(SettingModel())).copyWithData(
                newData = SettingModel(
                    menusModel = _settingMenuLiveData.value?.data,
                    isGranted = it
                )
            )
        }
    }

    fun requestSettingMenu(accessToken: String) {
        requestSettingMenusJob?.cancel()
        requestSettingMenusJob = viewModelScope.launch {
            _settingMenuLiveData.value = ModelState.Loading()
            notificationRepository.requestNotificationSettings(accessToken = accessToken)
                .onSuccess {
                    val model = asSettingMenusModel(settings = it ?: emptyMap())
                    _settingMenuLiveData.value = ModelState.Success(data = model)
                }.onError { exception, _ ->
                    _settingMenuLiveData.value = ModelState.Error(exception = exception)
                    _errorEventLiveData.value = EventWrapper(exception)
                }
        }
    }

    fun setPermissionGranted(isGranted: Boolean) {
        _isGrantedPermissionLiveData.value = isGranted
    }

    fun checkAllMenu(accessToken: String, isChecked: Boolean) {
        val menus =
            settingMenusLiveData.value?.data?.menusModel?.typeToCheckedMap?.map { it.key } ?: return
        switchMenu(accessToken = accessToken, types = menus, isChecked = isChecked)
    }

    fun checkMenu(accessToken: String, type: SettingMenuType, isChecked: Boolean) {
        switchMenu(accessToken = accessToken, types = listOf(type), isChecked = isChecked)
    }

    private fun switchMenu(accessToken: String, types: List<SettingMenuType>, isChecked: Boolean) {
        if (requestSettingMenusJob?.isCompleted == false) return
        requestSettingMenusJob = viewModelScope.launch {
            notificationRepository.requestSwitchNotificationSettings(
                accessToken = accessToken,
                settingsMap = types.associate { it.dataString to isChecked }
            ).onSuccess {
                val prevData = _settingMenuLiveData.value?.data ?: return@onSuccess
                val updatedMap = LinkedHashMap(prevData.typeToCheckedMap).apply {
                    types.forEach {
                        put(it, isChecked)
                    }
                }
                _settingMenuLiveData.value =
                    ModelState.Success(
                        prevData.copy(
                            typeToCheckedMap = updatedMap,
                            isAllMenuChecked = updatedMap.any { it.value }
                        )
                    )
            }.onError { exception, _ ->
                _errorEventLiveData.value = EventWrapper(exception)
            }
        }
    }
}