package com.polzzak_android.presentation.feature.notification

import android.text.SpannableString
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.feature.notification.model.NotificationModel
import com.polzzak_android.presentation.feature.notification.model.NotificationsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor() : ViewModel() {
    private val _notificationLiveData = MutableLiveData<ModelState<NotificationsModel>>()
    val notificationLiveData: LiveData<ModelState<NotificationsModel>> = _notificationLiveData
    private var requestNotificationJob: Job? = null

    init {
        requestNotifications(hasPriority = true)
    }

    fun requestMoreNotifications() {
        requestNotifications(hasPriority = false)
    }

    private fun requestNotifications(hasPriority: Boolean) {
        if (hasPriority) requestNotificationJob?.cancel()
        else if (requestNotificationJob?.isCompleted == false) return
        requestNotificationJob = viewModelScope.launch {
            val prevData = notificationLiveData.value?.data ?: NotificationsModel()
            _notificationLiveData.value = ModelState.Loading(prevData)
            //TODO api 호출
            delay(2000)
            val nextData = getMockData(prevData.nextOffset, NOTIFICATION_PAGE_SIZE)
            _notificationLiveData.value =
                ModelState.Success(nextData.copy(items = prevData.items + nextData.items))
        }

    }

    companion object {
        const val NOTIFICATION_PAGE_SIZE = 10
    }
}

private fun getMockData(nextOffset: Int, pageSize: Int): NotificationsModel {
    return NotificationsModel(
        hasNextPage = nextOffset + pageSize >= mockNotification.size,
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
            date = "${it}일 전",
            content = SpannableString("연동 완료"),
            nickName = "닉네임${it}",
            profileImageUrl = "https://picsum.photos/id/${it + 1}/200/300"
        )

        1 -> NotificationModel.LevelDown(
            date = "${it}일 전",
            content = SpannableString("레벨 감소"),
        )

        2 -> NotificationModel.LevelUp(
            date = "${it}일 전",
            content = SpannableString("레벨 업")
        )

        else -> NotificationModel.RequestLink(
            date = "${it}일 전",
            content = SpannableString("연동 요청"),
            nickName = "닉네임${it}",
            profileImageUrl = "https://picsum.photos/id/${it + 1}/200/300"
        )
    }
}