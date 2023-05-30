package com.polzzak_android.presentation.main.protector

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.polzzak_android.data.remote.model.StampBoardGroup
import com.polzzak_android.data.remote.model.response.StampBoardListResponse
import com.polzzak_android.data.repository.StampRepository
import com.polzzak_android.presentation.main.model.Partner
import com.polzzak_android.presentation.main.model.StampBoard
import com.polzzak_android.presentation.main.model.StampBoardSummary
import com.polzzak_android.presentation.main.model.convertToPartner
import com.polzzak_android.presentation.main.model.convertToStampBoardSummary
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProtectorStampViewModel @Inject constructor(
    private val repository: StampRepository
) : ViewModel() {

    private var _stampList: MutableLiveData<List<StampBoard>?> =
        MutableLiveData()
    val stampList = _stampList

    suspend fun setStampList(group: StampBoardGroup) {
        val response = repository.getStampBoardList(stampBoardGroup = group, memberId = "")

        response.onSuccess { list ->
            val data = list?.map { board ->
                StampBoard(
                    type = if (board.stampBoardSummaries.isEmpty()) 1 else 2,
                    partner = convertToPartner(board.partner),
                    stampBoardSummaries = board.stampBoardSummaries.map {
                        convertToStampBoardSummary(it)
                    }
                )
            }
            _stampList.value = data
        }

    }
}