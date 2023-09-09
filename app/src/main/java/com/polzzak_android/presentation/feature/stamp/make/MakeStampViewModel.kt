package com.polzzak_android.presentation.feature.stamp.make

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polzzak_android.common.util.safeLet
import com.polzzak_android.data.remote.model.request.MakeStampBoardRequest
import com.polzzak_android.data.remote.model.response.toStampBoardModel
import com.polzzak_android.data.repository.StampBoardRepository
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.feature.stamp.model.MakeStampCountModel
import com.polzzak_android.presentation.feature.stamp.model.MakeStampMissionListModel
import com.polzzak_android.presentation.feature.stamp.model.MakeStampNameModel
import com.polzzak_android.presentation.feature.stamp.model.MakeStampRewardModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MakeStampViewModel @Inject constructor(
    private val repository: StampBoardRepository
) : ViewModel() {

    enum class MakeStampBardInputType {
        NAME, REWARD, COUNT, MISSION_LIST
    }

    private var _selectedKidId: Int = 0

    private val _stampBoardName: MutableLiveData<MakeStampNameModel> = MutableLiveData()
    val stampBoardName = _stampBoardName

    private val _stampBoardReward: MutableLiveData<MakeStampRewardModel> = MutableLiveData()
    val stampBoardReward = _stampBoardReward

    private val _stampCount: MutableLiveData<MakeStampCountModel> = MutableLiveData()
    val stampCount = _stampCount

    private val _missionList: MutableLiveData<MakeStampMissionListModel> = MutableLiveData()
    val missionList = _missionList

    private val _missionListSize: MutableLiveData<Int> = MutableLiveData()
    val missionListSize = _missionListSize

    // 도장판 데이터
    private val _makeStampBoardState: MutableLiveData<ModelState<String>> = MutableLiveData()
    val makeStampBoardState = _makeStampBoardState

    init {
        initData()
    }

    fun initData() {
        _stampBoardName.value = MakeStampNameModel.init
        _stampBoardReward.value = MakeStampRewardModel.init
        _stampCount.value = MakeStampCountModel.init

        _missionList.value = MakeStampMissionListModel.init
        _missionListSize.value = _missionList.value!!.missionList.size
    }

    fun setSelectedKidId(id: Int) {
        _selectedKidId = id
    }

    /**
     * 유효성 체크
     */
    fun validateInput(token: String) {
        val checkName = validateInput(MakeStampBardInputType.NAME)
        val checkReward = validateInput(MakeStampBardInputType.REWARD)
        val checkCount = validateInput(MakeStampBardInputType.COUNT)
        val checkMission = validateInput(MakeStampBardInputType.MISSION_LIST)

        if (checkName && checkReward && checkCount && checkMission) {
            safeLet(
                _selectedKidId,
                _stampBoardName.value,
                _stampCount.value,
                _stampBoardReward.value,
                _missionList.value
            ) { id, name, count, reward, missionList ->
                makeStampBoard(
                    token = token,
                    id = id,
                    nameModel = name,
                    countModel = count,
                    rewardModel = reward,
                    missionListModel = missionList
                )
            } ?: run {
                // todo : 유효성 체크
            }
        }
    }

    private fun makeStampBoard(
        token: String,
        id: Int,
        nameModel: MakeStampNameModel,
        countModel: MakeStampCountModel,
        rewardModel: MakeStampRewardModel,
        missionListModel: MakeStampMissionListModel
    ) {
        val request = MakeStampBoardRequest(
            kidId = id,
            stampBoardName = nameModel.name,
            stampBoardCount = countModel.count,
            stampBoardReward = rewardModel.reward,
            missionList = missionListModel.missionList
        )

        viewModelScope.launch {
            _makeStampBoardState.value = ModelState.Loading()
            val response = repository.makeStampBoard(
                accessToken = token,
                newStampBoard = request
            )
            response.onSuccess {
                _makeStampBoardState.value = ModelState.Success("도장판 생성 성공")
            }.onError { exception, nothing ->
                _makeStampBoardState.value = ModelState.Error(exception)
            }
        }
    }

    /**
     * 도장판 이름
     */
    fun setStampBoardName(input: String) {
        val currentName = _stampBoardName.value
        _stampBoardName.value = currentName!!.copy(
            name = input
        )
    }

    /**
     * 도장판 보상
     */
    fun setStampBoardReward(input: String) {
        val currentReward = _stampBoardReward.value
        _stampBoardReward.value = currentReward!!.copy(
            reward = input
        )
    }

    /**
     * 도장판 개수
     */
    fun setStampCount(input: Int) {
        val currentCount = _stampCount.value
        _stampCount.value = currentCount!!.copy(
            count = input
        )
    }

    fun getStampCountList() = _stampCount.value!!.count

    /**
     * 미션
     */
    fun createMission() {
        val currentMissionList = _missionList.value
        val mutableMissionList = currentMissionList!!.missionList.toMutableList()

        mutableMissionList.add("")

        _missionList.value = currentMissionList.copy(
            missionList = mutableMissionList
        )
        _missionListSize.value = mutableMissionList.size
    }

    fun updateMissionList(input: List<String>) {
        val currentMissionList = _missionList.value
        _missionList.value = currentMissionList!!.copy(
            missionList = input
        )
    }

    fun deleteMission(mission: String) {
        val currentMissionList = _missionList.value
        val mutableMissionList = currentMissionList!!.missionList.toMutableList()

        mutableMissionList.remove(mission)

        _missionList.value = currentMissionList.copy(
            missionList = mutableMissionList
        )
        _missionListSize.value = mutableMissionList.size
    }

    fun setMissionListSize(size: Int) {
        _missionListSize.value = size
    }

    fun getMissionListSize() = _missionList.value!!.missionList.size

    /**
     * 유효성 체크
     */
    private fun validateInput(type: MakeStampBardInputType): Boolean {
        when (type) {
            MakeStampBardInputType.NAME -> {
                val result = MakeStampValidator.checkName(_stampBoardName.value!!.name)
                val isValidate = result.first
                val currentName = _stampBoardName.value

                if (isValidate) {
                    _stampBoardName.value = currentName!!.copy(
                        isValidate = true,
                        errorMessage = null
                    )
                } else {
                    _stampBoardName.value = currentName!!.copy(
                        isValidate = false,
                        errorMessage = result.second
                    )
                }

                return isValidate
            }

            MakeStampBardInputType.REWARD -> {
                val result = MakeStampValidator.checkReward(_stampBoardReward.value!!.reward)
                val isValidate = result.first
                val currentReward = _stampBoardReward.value

                if (isValidate) {
                    _stampBoardReward.value = currentReward!!.copy(
                        isValidate = true,
                        errorMessage = null
                    )
                } else {
                    _stampBoardReward.value = currentReward!!.copy(
                        isValidate = false,
                        errorMessage = result.second
                    )
                }

                return isValidate
            }

            MakeStampBardInputType.COUNT -> {
                val result = MakeStampValidator.checkCount(_stampCount.value!!.count)
                val isValidate = result.first
                val currentCount = _stampCount.value

                if (isValidate) {
                    _stampCount.value = currentCount!!.copy(
                        isValidate = true,
                        errorMessage = null
                    )
                } else {
                    _stampCount.value = currentCount!!.copy(
                        isValidate = false,
                        errorMessage = result.second
                    )
                }

                return isValidate
            }

            MakeStampBardInputType.MISSION_LIST -> {
                val result = MakeStampValidator.checkMission(_missionList.value!!.missionList)
                val isValidate = result.first
                val currentMissionList = _missionList.value

                if (isValidate) {
                    _missionList.value = currentMissionList!!.copy(
                        isValidate = true,
                        errorMessage = null,
                        errorPosition = null
                    )
                } else {
                    val resultValue = result.second

                    _missionList.value = currentMissionList!!.copy(
                        isValidate = false,
                        errorMessage = resultValue.message,
                        errorPosition = resultValue.index
                    )
                }

                return isValidate
            }
        }
    }
}