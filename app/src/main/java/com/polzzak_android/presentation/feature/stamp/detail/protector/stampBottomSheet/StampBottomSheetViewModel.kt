package com.polzzak_android.presentation.feature.stamp.detail.protector.stampBottomSheet

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polzzak_android.data.repository.StampBoardRepository
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.feature.stamp.model.MissionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StampBottomSheetViewModel @Inject constructor(
        private val stampRepository: StampBoardRepository
) : ViewModel() {

    // 파트너 id
    private var _partnerId: Int = 0
    fun setPartnerId(id: Int) {
        _partnerId = id
    }

    // 현재 스텝
    private val _isFirstStep: MutableLiveData<Boolean> = MutableLiveData(true)
    val isFirstStep get() = _isFirstStep
    fun updateStep(isFirstStep: Boolean) {
        _isFirstStep.value = isFirstStep
    }

    // 선택된 미션
    private val _selectedMission: MutableLiveData<MissionModel> = MutableLiveData()
    val selectedMission get() = _selectedMission
    fun setSelectedMission(missionModel: MissionModel) {
        _selectedMission.value = missionModel
    }

    // 선택된 도장
    private var _selectedStamp: MutableLiveData<CompleteStampModel> = MutableLiveData(getCompleteStampList().first())
    val selectedStamp get() = _selectedStamp
    fun setSelectedStamp(stamp: CompleteStampModel) {
        _selectedStamp.value = stamp
    }

    // 도장 찍어주기 성공 여부
    private val _makeStampSuccess: MutableLiveData<ModelState<Boolean?>> = MutableLiveData()
    val makeStampSuccess get() = _makeStampSuccess

    /**
     * 도장 생성 (도장 찍어주기)
     */
    fun makeStamp(accessToken: String, stampBoardId: Int) {
        val missionId = _selectedMission.value?.id ?: 0
        val stampId = _selectedStamp.value?.id ?: 0

        viewModelScope.launch {
            _makeStampSuccess.value = ModelState.Loading()
            val response = stampRepository.makeStamp(
                accessToken = accessToken,
                stampBoardId = stampBoardId,
                missionId = missionId,
                stampDesignId = stampId
            )

            response.onSuccess {
                _makeStampSuccess.value = ModelState.Success(true)
            }.onError { exception, unit ->
                _makeStampSuccess.value = ModelState.Error(exception)
            }
        }
    }
}