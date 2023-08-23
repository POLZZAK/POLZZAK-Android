package com.polzzak_android.presentation.feature.notification

import android.text.SpannableString
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.feature.notification.list.NotificationItemStateController
import com.polzzak_android.presentation.feature.notification.list.model.NotificationModel
import com.polzzak_android.presentation.feature.notification.list.model.NotificationRefreshStatusType
import com.polzzak_android.presentation.feature.notification.list.model.NotificationsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

//TODO pull to refresh, 더 불러오기 동시에 일어나면?
@HiltViewModel
class NotificationViewModel @Inject constructor() : ViewModel(), NotificationItemStateController {
    private val _notificationLiveData = MutableLiveData<ModelState<NotificationsModel>>()
    val notificationLiveData: LiveData<ModelState<NotificationsModel>> = _notificationLiveData
    private var requestNotificationJobData: NotificationJobData? = null

    var isRefreshed = false
        private set

    private val notificationHorizontalScrollPositionMap = HashMap<Int, Int>()

    init {
        initNotifications()
    }

    private fun initNotifications() {
        val priority = INIT_NOTIFICATIONS_PRIORITY
        if (requestNotificationJobData.getPriorityOrZero() < priority) requestNotificationJobData?.job?.cancel()
        else if (requestNotificationJobData?.job?.isCompleted == false) return
        requestNotificationJobData = NotificationJobData(
            priority = priority,
            job = viewModelScope.launch {
                isRefreshed = true
                _notificationLiveData.value = ModelState.Loading(NotificationsModel())
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
            job = viewModelScope.launch {
                isRefreshed = true
                val prevData = notificationLiveData.value?.data ?: NotificationsModel()
                _notificationLiveData.value =
                    ModelState.Loading(prevData.copy(refreshStatusType = NotificationRefreshStatusType.Loading))
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
            job = viewModelScope.launch {
                isRefreshed = false
                val prevData = notificationLiveData.value?.data ?: NotificationsModel()
                _notificationLiveData.value =
                    ModelState.Loading(prevData.copy(refreshStatusType = NotificationRefreshStatusType.Normal))
                requestNotifications()
            },
        )
    }

    //TODO test용 delay를 위해 suspend 붙여줌(제거 필요)
    private suspend fun requestNotifications() {
        val prevData =
            notificationLiveData.value?.data.takeIf { !isRefreshed } ?: NotificationsModel()
        //TODO api 연동(현재 mock data)
        //onSuccess
        delay(2000)
        val nextData = getMockData(prevData.nextOffset, NOTIFICATION_PAGE_SIZE)
        if (isRefreshed) notificationHorizontalScrollPositionMap.clear()
        _notificationLiveData.value =
            ModelState.Success(
                nextData.copy(
                    items = (prevData.items ?: emptyList()) + (nextData.items ?: emptyList()),
                    refreshStatusType = NotificationRefreshStatusType.Normal
                )
            )
    }

    fun deleteNotification() {
        //TODO 알림 삭제 문의 중
    }

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

private fun getMockData(nextOffset: Int, pageSize: Int): NotificationsModel {
    return NotificationsModel(
        hasNextPage = nextOffset + pageSize < mockNotification.size,
        nextOffset = nextOffset + pageSize,
        items = mockNotification.subList(
            nextOffset,
            minOf(mockNotification.size, nextOffset + pageSize)
        )
    )
}

private val mockNotification = List(187) {
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