package com.polzzak_android.presentation.feature.stamp.main.protector

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polzzak_android.data.remote.model.response.toStampBoardModel
import com.polzzak_android.data.repository.StampBoardRepository
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.feature.stamp.model.StampBoardModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StampViewModel @Inject constructor(
    private val stampBoardRepository: StampBoardRepository
) : ViewModel() {

    // 도장판 리스트
    private val _stampBoardList: MutableLiveData<ModelState<List<StampBoardModel>>> =
        MutableLiveData()
    val stampBoardList get() = _stampBoardList

    // 선택한 유저
    private val _selectedUserId: MutableLiveData<String?> = MutableLiveData()
    val selectedUserId get() = _selectedUserId

    /**
     * 도장판 조회
     */
    fun requestStampBoardList(accessToken: String, linkedMemberId: String?, stampBoardGroup: String) {
        viewModelScope.launch {
            _stampBoardList.value = ModelState.Loading()
            stampBoardRepository.getStampBoardList(accessToken, linkedMemberId, stampBoardGroup)
                .onSuccess { data ->
                    val stampBoardList = data?.map {
                        it.toStampBoardModel()
                    }
                    _stampBoardList.value = ModelState.Success(stampBoardList!!)
                }.onError { exception, data ->
                _stampBoardList.value = ModelState.Error(exception)
            }
        }
    }

    fun getSelectedUserId() = _selectedUserId.value
    fun setSelectedUserId(userId: Int?) {
        _selectedUserId.value = userId?.toString()
    }
}