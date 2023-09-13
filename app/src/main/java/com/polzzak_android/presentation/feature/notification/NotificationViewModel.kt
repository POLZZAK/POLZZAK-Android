package com.polzzak_android.presentation.feature.notification

import android.text.SpannableString
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.model.copyWithData
import com.polzzak_android.presentation.feature.notification.list.NotificationItemStateController
import com.polzzak_android.presentation.feature.notification.list.model.NotificationModel
import com.polzzak_android.presentation.feature.notification.list.model.NotificationRefreshStatusType
import com.polzzak_android.presentation.feature.notification.list.model.NotificationsModel
import com.polzzak_android.presentation.feature.notification.setting.model.SettingMenuType
import com.polzzak_android.presentation.feature.notification.setting.model.SettingMenusModel
import com.polzzak_android.presentation.feature.notification.setting.model.SettingModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor() : ViewModel(), NotificationItemStateController {
    private val _notificationLiveData = MutableLiveData<ModelState<NotificationsModel>>()
    val notificationLiveData: LiveData<ModelState<NotificationsModel>> = _notificationLiveData
    private var requestNotificationJobData: NotificationJobData? = null

    //TODO 추가 삭제 등 알림목록 수정 이벤트
    private var updateNotificationJobMap = HashMap<Int, Job?>()

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
                requestNotifications()
            },
        )
    }

    fun refreshNotifications() {
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
                requestNotifications()
            },
        )
    }

    fun requestMoreNotifications() {
        if (notificationLiveData.value?.data?.hasNextPage == false) return
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
                requestNotifications()
            },
        )
    }

    //TODO test용 delay를 위해 suspend 붙여줌(제거 필요)
    private suspend fun requestNotifications() {
        //TODO api 연동(현재 mock data)
        val nextOffset = notificationLiveData.value?.data?.nextOffset.takeIf { !isRefreshed } ?: 0
        delay(2000)
        val nextData = getMockNotificationData(nextOffset, NOTIFICATION_PAGE_SIZE)

        //onSuccess
        notificationMutex.lock()
        val prevData =
            notificationLiveData.value?.data.takeIf { !isRefreshed } ?: NotificationsModel()
        if (isRefreshed) notificationHorizontalScrollPositionMap.clear()
        _notificationLiveData.value =
            ModelState.Success(
                nextData.copy(
                    items = (prevData.items ?: emptyList()) + (nextData.items ?: emptyList()),
                    refreshStatusType = NotificationRefreshStatusType.Normal
                )
            )
        notificationMutex.unlock()
    }

    fun deleteNotification(id: Int) {
        if (updateNotificationJobMap[id]?.isCompleted == false) return
        updateNotificationJobMap[id] = createJobWithUnlockOnCompleted(mutex = notificationMutex) {
            //TODO api 적용
            delay(1000)
            deleteMockNotificationData(id = id)

            //onSuccess
            notificationMutex.lock()
            val updatedList = notificationLiveData.value?.data?.items?.toMutableList()?.apply {
                removeIf { it.id == id }
            }
            val updatedData =
                notificationLiveData.value?.data?.copy(items = updatedList) ?: NotificationsModel()
            _notificationLiveData.value =
                _notificationLiveData.value?.copyWithData(newData = updatedData)
            notificationMutex.unlock()
            //TODO onError 이벤트 추가(Livedata, eventWrapper 등 필요할 수도 있음)
        }
    }

    fun addNotification(model: NotificationModel) {
        if (updateNotificationJobMap[model.id]?.isCompleted == false) return
        updateNotificationJobMap[model.id] =
            createJobWithUnlockOnCompleted(mutex = notificationMutex) {
                //TODO 푸쉬알림으로 인한 알림 목록 추가

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

    companion object {
        const val NOTIFICATION_PAGE_SIZE = 10
        private const val INIT_NOTIFICATIONS_PRIORITY = 3
        private const val REFRESH_NOTIFICATIONS_PRIORITY = 2
        private const val MORE_NOTIFICATIONS_PRIORITY = 1
    }
}

private fun getMockNotificationData(nextOffset: Int, pageSize: Int): NotificationsModel {
    return NotificationsModel(
        hasNextPage = nextOffset + pageSize < mockNotification.size,
        nextOffset = nextOffset + pageSize,
        items = mockNotification.subList(
            nextOffset,
            minOf(mockNotification.size, nextOffset + pageSize)
        )
    )
}

private fun deleteMockNotificationData(id: Int) {
    mockNotification.removeIf { it.id == id }
}

private val mockNotification = MutableList(187) {
    when (it % 4) {
        0 -> NotificationModel.CompleteLink(
            id = it,
            date = "${it}일 전",
            content = SpannableString("연동 완료"),
            nickName = "닉네임${it}",
            profileImageUrl = "https://picsum.photos/id/${it + 1}/200/300"
        )

        1 -> NotificationModel.LevelDown(
            id = it,
            date = "${it}일 전",
            content = SpannableString("레벨 감소"),
        )

        2 -> NotificationModel.LevelUp(
            id = it,
            date = "${it}일 전",
            content = SpannableString("레벨 업")
        )

        else -> NotificationModel.RequestLink(
            id = it,
            date = "${it}일 전",
            content = SpannableString("연동 요청"),
            nickName = "닉네임${it}",
            profileImageUrl = "https://picsum.photos/id/${it + 1}/200/300"
        )
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