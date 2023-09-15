package com.polzzak_android.presentation.feature.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.polzzak_android.common.util.livedata.EventWrapper
import com.polzzak_android.data.repository.FamilyRepository
import com.polzzak_android.data.repository.NotificationRepository
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.model.copyWithData
import com.polzzak_android.presentation.feature.notification.list.NotificationItemStateController
import com.polzzak_android.presentation.feature.notification.list.model.NotificationModel
import com.polzzak_android.presentation.feature.notification.list.model.NotificationRefreshStatusType
import com.polzzak_android.presentation.feature.notification.list.model.NotificationStatusType
import com.polzzak_android.presentation.feature.notification.list.model.NotificationsModel
import com.polzzak_android.presentation.feature.notification.list.model.toNotificationModel
import com.polzzak_android.presentation.feature.notification.setting.model.SettingMenuType
import com.polzzak_android.presentation.feature.notification.setting.model.SettingMenusModel
import com.polzzak_android.presentation.feature.notification.setting.model.SettingModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

class NotificationViewModel @AssistedInject constructor(
    private val notificationRepository: NotificationRepository,
    private val familyRepository: FamilyRepository,
    @Assisted private val initAccessToken: String
) : ViewModel(), NotificationItemStateController {
    private val _notificationLiveData = MutableLiveData<ModelState<NotificationsModel>>()
    val notificationLiveData: LiveData<ModelState<NotificationsModel>> = _notificationLiveData
    private var requestNotificationJobData: NotificationJobData? = null

    private val _errorEventLiveData = MutableLiveData<EventWrapper<Exception>>()
    val errorEventLiveData: LiveData<EventWrapper<Exception>> = _errorEventLiveData
    private val updateNotificationJobMap = HashMap<Int, Job?>()

    private val requestLinkJob = HashMap<Int, Job?>()
    var isRefreshed = false
        private set

    private val notificationHorizontalScrollPositionMap = HashMap<Int, Int>()

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

    private val notificationMutex = Mutex()
    private val settingMutex = Mutex()

    init {
        initNotifications()
    }

    private fun initNotifications() {
        val priority = INIT_NOTIFICATIONS_PRIORITY
        if (requestNotificationJobData.getPriorityOrZero() < priority) requestNotificationJobData?.job?.cancel()
        else if (requestNotificationJobData?.job?.isCompleted == false) return
        requestNotificationJobData = NotificationJobData(
            priority = priority,
            job = createJobWithUnlockOnCompleted(mutex = notificationMutex) {
                isRefreshed = true
                notificationMutex.lock()
                _notificationLiveData.value = ModelState.Loading(NotificationsModel())
                notificationMutex.unlock()
                requestNotifications(accessToken = initAccessToken)
            },
        )
    }

    fun refreshNotifications(accessToken: String) {
        val priority = REFRESH_NOTIFICATIONS_PRIORITY
        if (requestNotificationJobData.getPriorityOrZero() < priority) requestNotificationJobData?.job?.cancel()
        else if (requestNotificationJobData?.job?.isCompleted == false) return
        requestNotificationJobData = NotificationJobData(
            priority = priority,
            job = createJobWithUnlockOnCompleted(mutex = notificationMutex) {
                isRefreshed = true
                notificationMutex.lock()
                val prevData = notificationLiveData.value?.data ?: NotificationsModel()
                _notificationLiveData.value =
                    ModelState.Loading(prevData.copy(refreshStatusType = NotificationRefreshStatusType.Loading))
                notificationMutex.unlock()
                requestNotifications(accessToken = accessToken)
            },
        )
    }

    fun requestMoreNotifications(accessToken: String) {
        if (notificationLiveData.value?.data?.nextId == null) return
        val priority = MORE_NOTIFICATIONS_PRIORITY
        if (requestNotificationJobData.getPriorityOrZero() <= priority) requestNotificationJobData?.job?.cancel()
        else if (requestNotificationJobData?.job?.isCompleted == false) return
        requestNotificationJobData = NotificationJobData(
            priority = priority,
            job = createJobWithUnlockOnCompleted(mutex = notificationMutex) {
                isRefreshed = false
                notificationMutex.lock()
                val prevData = notificationLiveData.value?.data ?: NotificationsModel()
                _notificationLiveData.value =
                    ModelState.Loading(prevData.copy(refreshStatusType = NotificationRefreshStatusType.Normal))
                notificationMutex.unlock()
                requestNotifications(accessToken = accessToken)
            },
        )
    }

    private suspend fun requestNotifications(accessToken: String) {
        notificationRepository.requestNotifications(
            accessToken = accessToken,
            startId = notificationLiveData.value?.data?.nextId.takeIf { !isRefreshed },
        ).onSuccess {
            notificationMutex.lock()
            val prevData =
                notificationLiveData.value?.data.takeIf { !isRefreshed } ?: NotificationsModel()
            val updatedNotifications =
                prevData.copy(
                    nextId = it?.startId,
                    items = (prevData.items ?: emptyList()) + (it?.notificationDtoList
                        ?: emptyList()).mapNotNull { notificationDto -> notificationDto.toNotificationModel() },
                    refreshStatusType = NotificationRefreshStatusType.Normal
                )
            _notificationLiveData.value = ModelState.Success(updatedNotifications)
            notificationMutex.unlock()
        }.onError { exception, _ ->
            _errorEventLiveData.value = EventWrapper(exception)
        }
    }

    fun deleteNotification(accessToken: String, id: Int) {
        if (updateNotificationJobMap[id]?.isCompleted == false) return
        updateNotificationJobMap[id] = createJobWithUnlockOnCompleted(mutex = notificationMutex) {
            notificationRepository.deleteNotifications(
                accessToken = accessToken,
                notificationIds = listOf(id)
            ).onSuccess {
                notificationMutex.lock()
                _notificationLiveData.value = _notificationLiveData.value?.run {
                    val prevModel = data ?: return@onSuccess
                    val updatedList =
                        prevModel.items?.toMutableList()?.apply { removeIf { it.id == id } }
                    val updatedModel = prevModel.copy(items = updatedList)
                    copyWithData(updatedModel)
                }
                notificationMutex.unlock()
            }.onError { exception, _ ->
                _errorEventLiveData.value = EventWrapper(exception)
            }
        }
    }

    fun addNotification(model: NotificationModel) {
        if (updateNotificationJobMap[model.id]?.isCompleted == false) return
        updateNotificationJobMap[model.id] =
            createJobWithUnlockOnCompleted(mutex = notificationMutex) {
                //TODO 푸쉬알림으로 인한 알림 목록 추가

            }
    }

    fun requestApproveLinkRequest(accessToken: String, notificationModel: NotificationModel) {
        val userId = notificationModel.user?.userId ?: return
        if (requestLinkJob[userId]?.isCompleted == false) return
        requestLinkJob[userId] = createJobWithUnlockOnCompleted(mutex = notificationMutex) {
            familyRepository.requestApproveLinkRequest(accessToken = accessToken, targetId = userId)
                .onSuccess {
                    notificationMutex.lock()
                    _notificationLiveData.value = _notificationLiveData.value?.run {
                        val prevModel = data ?: return@onSuccess
                        val updatedList =
                            prevModel.items?.toMutableList()?.apply {
                                forEachIndexed { idx, model ->
                                    if (model.id == notificationModel.id) {
                                        this[idx] =
                                            this[idx].copy(statusType = NotificationStatusType.REQUEST_FAMILY_ACCEPT)
                                    }
                                }
                            }
                        val updatedModel = prevModel.copy(items = updatedList)
                        copyWithData(updatedModel)
                    }
                    notificationMutex.unlock()
                }.onError { exception, _ ->
                    _errorEventLiveData.value = EventWrapper(exception)
                }
        }
    }


    fun requestRejectLinkRequest(accessToken: String, notificationModel: NotificationModel) {
        val userId = notificationModel.user?.userId ?: return
        if (requestLinkJob[userId]?.isCompleted == false) return
        requestLinkJob[userId] = createJobWithUnlockOnCompleted(mutex = notificationMutex) {
            familyRepository.requestRejectLinkRequest(accessToken = accessToken, targetId = userId)
                .onSuccess {
                    notificationMutex.lock()
                    _notificationLiveData.value = _notificationLiveData.value?.run {
                        val prevModel = data ?: return@onSuccess
                        val updatedList =
                            prevModel.items?.toMutableList()?.apply {
                                forEachIndexed { idx, model ->
                                    if (model.id == notificationModel.id) {
                                        this[idx] =
                                            this[idx].copy(statusType = NotificationStatusType.REQUEST_FAMILY_REJECT)
                                    }
                                }
                            }
                        val updatedModel = prevModel.copy(items = updatedList)
                        copyWithData(updatedModel)
                    }
                    notificationMutex.unlock()
                }.onError { exception, _ ->
                    _errorEventLiveData.value = EventWrapper(exception)
                }
        }
    }

    private fun createJobWithUnlockOnCompleted(mutex: Mutex, action: suspend () -> Unit) =
        viewModelScope.launch {
            action.invoke()
        }.apply {
            invokeOnCompletion {
                if (mutex.isLocked) mutex.unlock()
            }
        }

    fun requestSettingMenu() {
        requestSettingMenusJob?.cancel()
        requestSettingMenusJob = viewModelScope.launch {
            //TOOD repository 구현
            _settingMenuLiveData.value = ModelState.Loading()
            delay(2000)
            _settingMenuLiveData.value =
                ModelState.Success(mockKidSettingMenus)
        }
    }

    fun setPermissionGranted(isGranted: Boolean) {
        _isGrantedPermissionLiveData.value = isGranted
    }

    fun checkMenu(type: SettingMenuType, isChecked: Boolean) {
        if (requestSettingMenusJob?.isCompleted == false) return
        requestSettingMenusJob = createJobWithUnlockOnCompleted(mutex = settingMutex) {
            //TODO setting 변경 api 호출
            delay(1000)
            //onSuccess
            _settingMenuLiveData.value = _settingMenuLiveData.value?.run {
                val newData = (data ?: SettingMenusModel()).run {
                    typeToCheckedMap[type] = isChecked
                    copy(isAllMenuChecked = typeToCheckedMap.any { it.value } || isAllMenuChecked)
                }
                copyWithData(newData)
            }
        }
    }

    fun checkAllMenu(isChecked: Boolean) {
        if (requestSettingMenusJob?.isCompleted == false) return
        requestSettingMenusJob = createJobWithUnlockOnCompleted(mutex = settingMutex) {
            //TODO setting 변경 api 호출
            delay(1000)
            _settingMenuLiveData.value = _settingMenuLiveData.value?.run {
                val newData = (data ?: SettingMenusModel()).run {
                    copy(
                        isAllMenuChecked = isChecked,
                        typeToCheckedMap = typeToCheckedMap.apply { replaceAll { _, _ -> isChecked } })
                }
                copyWithData(newData)
            }
        }
    }

    //TODO Setting 변경 request

    private data class NotificationJobData(val priority: Int, val job: Job)

    private fun NotificationJobData?.getPriorityOrZero() = this?.priority ?: 0

    override fun setHorizontalScrollPosition(id: Int, position: Int) {
        notificationHorizontalScrollPositionMap[id] = position
    }

    override fun getHorizontalScrollPosition(id: Int): Int =
        notificationHorizontalScrollPositionMap[id] ?: 0

    override fun getIsRefreshedSuccess(): Boolean =
        isRefreshed && notificationLiveData.value is ModelState.Success

    @AssistedFactory
    interface NotificationAssistedFactory {
        fun create(initAccessToken: String): NotificationViewModel
    }

    companion object {
        private const val INIT_NOTIFICATIONS_PRIORITY = 3
        private const val REFRESH_NOTIFICATIONS_PRIORITY = 2
        private const val MORE_NOTIFICATIONS_PRIORITY = 1
        fun provideFactory(
            notificationAssistedFactory: NotificationAssistedFactory,
            initAccessToken: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return notificationAssistedFactory.create(initAccessToken = initAccessToken) as T
            }
        }
    }
}

private val mockKidSettingMenus = SettingMenusModel(
    hashMapOf(
        SettingMenuType.Link to false,
        SettingMenuType.Level to false,
        SettingMenuType.NewStampBoard to true,
        SettingMenuType.PaymentCoupon to false,
        SettingMenuType.CheckDeliveryGift to false,
    ),
    isAllMenuChecked = true,
)