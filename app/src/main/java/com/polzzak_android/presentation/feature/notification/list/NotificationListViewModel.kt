package com.polzzak_android.presentation.feature.notification.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.polzzak_android.common.util.livedata.EventWrapper
import com.polzzak_android.data.repository.FamilyRepository
import com.polzzak_android.data.repository.NotificationRepository
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.model.copyWithData
import com.polzzak_android.presentation.feature.notification.list.model.NotificationModel
import com.polzzak_android.presentation.feature.notification.list.model.NotificationStatusType
import com.polzzak_android.presentation.feature.notification.list.model.NotificationsModel
import com.polzzak_android.presentation.feature.notification.list.model.toNotificationModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

class NotificationListViewModel @AssistedInject constructor(
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
    private val notificationMutex = Mutex()

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
                    ModelState.Loading(prevData.copy(isRefreshable = true))
                notificationMutex.unlock()
                notificationHorizontalScrollPositionMap.clear()
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
                    ModelState.Loading(prevData.copy(isRefreshable = true))
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
                    isRefreshable = true
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
        fun create(initAccessToken: String): NotificationListViewModel
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