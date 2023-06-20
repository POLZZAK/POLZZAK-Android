package com.polzzak_android.presentation.makingStamp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MakeStampViewModel @Inject constructor() : ViewModel() {

    private val _selectedKidId: Int = 0

    private val _stampBoardName: MutableLiveData<String> = MutableLiveData()

    private val _stampBoardReward: MutableLiveData<String> = MutableLiveData()

    private val _stampCountList: MutableLiveData<Int> = MutableLiveData()
    val stampCountList = _stampCountList

    private val _missionList: MutableLiveData<List<String>> = MutableLiveData()
    val missionList = _missionList

    private val _missionListSize: MutableLiveData<Int> = MutableLiveData()
    val missionListSize = _missionListSize

    init {
        setStampCountList(0)
        _missionList.value = listOf("test1", "test2")
        _missionListSize.value = _missionList.value?.size ?: 0
    }

    fun postStampBoardInfo() {
        val data = 0
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
    fun setStampCountList(value: Int) {
        _stampCountList.value = value
    }

    fun getStampCountList() = _stampCountList.value

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