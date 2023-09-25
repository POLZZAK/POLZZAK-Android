package com.polzzak_android.presentation.feature.myPage.protector.point.ranking

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polzzak_android.data.repository.PointRepository
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.feature.myPage.model.RankingScreenModel
import com.polzzak_android.presentation.feature.myPage.model.toModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProtectorRankingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: PointRepository
) : ViewModel() {

    private val _rankingListFlow = MutableSharedFlow<ModelState<RankingScreenModel>>(replay = 1)
    val rankingScreenModel
        get() = _rankingListFlow.asSharedFlow()

    init {
        val token = savedStateHandle.get<String>("token") ?: ""
        getRankingList(token = token)
    }

    private fun getRankingList(token: String) = viewModelScope.launch {
        repository
            .getProtectorRankingList(token)
            .onSuccess { data ->
                data ?: return@onSuccess

                _rankingListFlow.emit(ModelState.Success(data.toModel()))
            }
            .onError { exception, _ ->
                exception.printStackTrace()

                _rankingListFlow.emit(ModelState.Error(exception))
            }
    }
}