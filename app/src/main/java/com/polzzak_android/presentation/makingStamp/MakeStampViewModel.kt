package com.polzzak_android.presentation.makingStamp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polzzak_android.common.util.safeLet
import com.polzzak_android.data.remote.model.request.MakeStampBoardRequest
import com.polzzak_android.data.repository.StampBoardRepository
import com.polzzak_android.presentation.common.model.ModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MakeStampViewModel @Inject constructor(
    private val repository: StampBoardRepository
) : ViewModel() {

    private var _selectedKidId: Int = 0

    private val _stampBoardName: MutableLiveData<String> = MutableLiveData()

    private val _stampBoardReward: MutableLiveData<String> = MutableLiveData()

    private val _stampCount: MutableLiveData<Int> = MutableLiveData()
    val stampCount = _stampCount

    private val _missionList: MutableLiveData<List<String>> = MutableLiveData()
    val missionList = _missionList

    private val _missionListSize: MutableLiveData<Int> = MutableLiveData()
    val missionListSize = _missionListSize

    // 도장판 데이터
    private val _makeStampBoardState: MutableLiveData<ModelState<String>> = MutableLiveData()
    val makeStampBoardState = _makeStampBoardState

    init {
        setStampCount(0)
        _missionList.value = listOf("test1", "test2")
        _missionListSize.value = _missionList.value?.size ?: 0

        // todo : 임시
        _stampBoardName.value = "임시 보드 이름"
        _stampBoardReward.value = "임시 보드 보상값"
    }

    fun makeStampBoard() {
        safeLet(_selectedKidId, _stampBoardName.value, _stampCount.value, _stampBoardReward.value, _missionList.value) {
            id, name, count, reward, missionList ->
            val request = MakeStampBoardRequest(
                kidId = id,
                stampBoardName = name,
                stampBoardCount = count,
                stampBoardReward = reward,
                missionList = missionList
            )

            viewModelScope.launch {
                _makeStampBoardState.postValue(ModelState.Loading())
                val response = repository.makeStampBoard(
                    // todo: 임시 토큰
                    token = "",
                    newStampBoard = request)
                response.onSuccess {
                    _makeStampBoardState.postValue(ModelState.Success("도장판 생성 성공"))
                }.onError { exception, nothing ->
                    _makeStampBoardState.postValue(ModelState.Error(exception))
                }
            }
        } ?: run {
            // todo : 유효성 체크
        }
    }

    /**
     * 도장판 이름
     */
    fun setStampBoardName(name: String) {
        _stampBoardName.value = name
    }

    /**
     * 도장판 보상
     */
    fun setStampBoardReward(reward: String) {
        _stampBoardReward.value = reward
    }

    /**
     * 도장판 개수
     */
    fun setStampCount(value: Int) {
        _stampCount.value = value
    }

    fun getStampCountList() = _stampCount.value

    /**
     * 미션
     */
    fun createMission() {
        val currentMissionList = _missionList.value.orEmpty().toMutableList()
        currentMissionList.add("")
        _missionList.value = currentMissionList
        _missionListSize.value = currentMissionList.size
    }

    fun updateMissionList(missionList: List<String>) {
        _missionList.value = missionList
    }

    fun addMissionList(newMissionList: List<String>) {
        val currentMissionList = _missionList.value.orEmpty().toMutableList()
        currentMissionList.addAll(newMissionList)
        _missionList.value = currentMissionList
        _missionListSize.value = currentMissionList.size
    }

    fun deleteMission(mission: String) {
        val currentMissionList = _missionList.value.orEmpty().toMutableList()
        currentMissionList.remove(mission)
        _missionList.value = currentMissionList
        _missionListSize.value = currentMissionList.size
    }

    fun setMissionListSize(size: Int) {
        _missionListSize.value = size
    }

    fun getMissionListSize() = _missionList.value?.size ?: 0
}