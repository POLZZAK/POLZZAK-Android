package com.polzzak_android.presentation.feature.stamp.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polzzak_android.data.repository.StampBoardRepository
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.feature.stamp.model.StampBoardDetailModel
import com.polzzak_android.presentation.feature.stamp.model.toModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StampBoardDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val stampRepository: StampBoardRepository
) : ViewModel() {
    private val _stampBoardData = MutableStateFlow<ModelState<StampBoardDetailModel>>(ModelState.Loading())
    val stampBoardData
        get() = _stampBoardData.asStateFlow()

    private val partnerId: Int
    val partnerType: String

    private val stampBoardId: Int
        get() = stampBoardData.value.data?.stampBoardId ?: -1

    init {
        partnerId = savedStateHandle.get<Int>("partnerId") ?: -1
        partnerType = savedStateHandle.get<String>("partnerType") ?: ""

        val token = savedStateHandle.get<String>("token") ?: ""
        val boardId = savedStateHandle.get<Int>("boardId") ?: -1
        fetchStampBoardDetailData(accessToken = token, stampBoardId = boardId)
    }

    fun fetchStampBoardDetailData(
        accessToken: String,
        stampBoardId: Int
    ) = viewModelScope.launch(Dispatchers.IO) {
        stampRepository
            .getStampBoardDetailData(
                accessToken = accessToken,
                stampBoardId = stampBoardId
            )
            .onSuccess { data ->
                data ?: return@onSuccess

                _stampBoardData.update {
                    ModelState.Success(data.toModel())
                }
            }
            .onError { exception, data ->
                Timber.e(exception)

                _stampBoardData.update {
                    ModelState.Error(exception)
                }
            }
    }

    /**
     * (아이 전용) 보호자에게 도장 요청.
     *
     * @param accessToken 사용자 토큰
     * @param missionId 완료한 미션의 id
     * @param onStart api 호출 전 동작
     * @param onCompletion api 응답 왔을 때 동작
     */
    fun requestStampToProtector(
        accessToken: String,
        missionId: Int,
        onStart: () -> Unit,
        onCompletion: (cause: Throwable?) -> Unit
    ) = viewModelScope.launch {
        onStart()

        stampRepository
            .requestStampToProtector(
                accessToken = accessToken,
                stampBoardId = stampBoardId,
                missionId =  missionId,
                guardianId = partnerId
            )
            .onSuccess {
                onCompletion(null)
            }
            .onError { exception, _ ->
                onCompletion(exception)
            }
    }
}