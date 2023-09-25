package com.polzzak_android.presentation.feature.myPage.point.history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polzzak_android.data.repository.PointRepository
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.feature.myPage.model.PointHistoryItemModel
import com.polzzak_android.presentation.feature.myPage.model.toModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PointHistoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: PointRepository
) : ViewModel() {

    /**
     * 다음에 호출해야 할 인덱스 아이디.
     */
    private var nextStartId: Int? = 0

    /**
     * 포인트 내역 리스트 Flow.
     */
    private val _historyListFlow = MutableStateFlow<MutableList<PointHistoryItemModel>>(mutableListOf())
    val historyListFlow
        get() = _historyListFlow.asStateFlow()

    /**
     * 화면 상태 Flow.
     * 내역 리스트를 [ModelState]로 감싸면 데이터를 업데이트하기 어려워 분리하여 관리합니다.
     */
    private val _screenState = MutableStateFlow<ModelState<Unit>>(ModelState.Loading())
    val screenState
        get() = _screenState.asStateFlow()

    init {
        val token = savedStateHandle.get<String>("token") ?: ""
        getNextHistoryList(token = token)
    }

    fun getNextHistoryList(token: String) = viewModelScope.launch {
        Timber.d(">> nextStartId = $nextStartId")

        // nextStartId가 null이면 다음에 호출할 리스트가 없다는 의미
        nextStartId?.also { startId ->
            repository
                .getPointHistoryList(token, startId)
                .onSuccess { dto ->
                    dto ?: return@onSuccess

                    nextStartId = dto.startId

                    val list = dto.historyList.map { it.toModel() }
                    _historyListFlow.value.addAll(list)

                    _screenState.emit(ModelState.Success(Unit))
                }
                .onError { exception, _ ->
                    exception.printStackTrace()

                    _screenState.emit(ModelState.Error(exception))
                }
        }
    }
}