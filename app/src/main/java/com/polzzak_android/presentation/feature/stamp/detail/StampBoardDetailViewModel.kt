package com.polzzak_android.presentation.feature.stamp.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polzzak_android.data.repository.StampBoardRepository
import com.polzzak_android.data.repository.StampRepository
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.feature.stamp.model.StampBoardDetailModel
import com.polzzak_android.presentation.feature.stamp.model.toModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StampBoardDetailViewModel @Inject constructor(
    private val stampRepository: StampBoardRepository
) : ViewModel() {
    private val _stampBoardData = MutableStateFlow<ModelState<StampBoardDetailModel>>(ModelState.Loading())
    val stampBoardData
        get() = _stampBoardData.asStateFlow()

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
}