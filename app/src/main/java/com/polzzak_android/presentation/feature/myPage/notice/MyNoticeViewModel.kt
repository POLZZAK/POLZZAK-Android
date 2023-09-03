package com.polzzak_android.presentation.feature.myPage.notice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.util.toLocalDate
import com.polzzak_android.presentation.feature.myPage.notice.model.MyNoticeModel
import com.polzzak_android.presentation.feature.myPage.notice.model.MyNoticesModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

//TODO api 연동
class MyNoticeViewModel : ViewModel() {
    private val _noticesLiveData = MutableLiveData<ModelState<MyNoticesModel>>()
    val noticesLiveData: LiveData<ModelState<MyNoticesModel>> = _noticesLiveData

    private var requestNoticesJob: Job? = null

    init {
        initNotices()
    }

    private fun initNotices() {
        requestNoticesJob?.cancel()
        requestNoticesJob = viewModelScope.launch {
            _noticesLiveData.value = ModelState.Loading(MyNoticesModel())
            requestNotices()
        }
    }

    fun requestMoreNotices() {
        if (noticesLiveData.value?.data?.hasNextPage == false) return
        if (requestNoticesJob?.isCompleted == false) return
        requestNoticesJob = viewModelScope.launch {
            _noticesLiveData.value =
                ModelState.Loading(_noticesLiveData.value?.data ?: MyNoticesModel())
            requestNotices()
        }
    }

    private suspend fun requestNotices() {
        val prevData = noticesLiveData.value?.data ?: MyNoticesModel()
        delay(2000)
        //TODO api 연동
        //on Success
        val nextData = getMockNotices(nextId = prevData.nextId, pageSize = PAGE_SIZE)
        val updatedData = nextData.copy(notices = prevData.notices + nextData.notices)
        _noticesLiveData.value = ModelState.Success(updatedData)
    }

    companion object {
        private const val PAGE_SIZE = 10
    }
}

private fun getMockNotices(nextId: Int?, pageSize: Int): MyNoticesModel {
    val startIdx = nextId ?: 0
    val nextIdx = minOf(mockNotices.size, startIdx + pageSize)
    val nId = mockNotices.getOrNull(nextIdx)?.id
    return MyNoticesModel(
        notices = mockNotices.subList(startIdx, nextIdx),
        nextId = nId,
        hasNextPage = (nId != null)
    )
}

private val mockNotices = List(27) {
    MyNoticeModel(
        id = it,
        title = "title$it".repeat((it % 12) + 1),
        date = "2023-06-04T20:08:23.745393551".toLocalDate() ?: (LocalDate.now()),
        content = "content \n\n\n content1 \n\n content \n"
    )
}