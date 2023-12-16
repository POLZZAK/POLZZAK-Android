package com.polzzak_android.presentation.component.newbottomsheet.makestamp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.polzzak_android.data.repository.StampBoardRepository
import com.polzzak_android.presentation.component.newbottomsheet.base.SheetEvent
import com.polzzak_android.presentation.feature.stamp.model.MissionData
import com.polzzak_android.presentation.feature.stamp.model.MissionRequestModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class MakeStampViewModel @AssistedInject constructor(
    @Assisted missionList: List<MissionData>,
    private val repository: StampBoardRepository
    /*@Assisted val missionList: List<MissionModel>,
    @Assisted val missionRequestList: List<MissionRequestModel>,*/
) : ViewModel() {

    init {
        Timber.d(">> created")
    }

    private val _missionList = missionList.toMutableList()
    val missionList: List<MissionData>
        get() = _missionList

    // 도장 요청 알림 바를 통해서 들어온건지 여부
    val isRequested: Boolean
        get() = missionList.firstOrNull() is MissionRequestModel

    // 미션 요청 아이디 =/= 미션 아이디
    // 구분하지 않고 저장 후 api 쏠때 구분
    private val _selectedMissionId = MutableStateFlow(-1)
    val selectedMissionId
        get() = _selectedMissionId.asStateFlow()

    // 선택한 도장 디자인 아이디
    private val _selectedStampDesignId = MutableStateFlow(1)
    val selectedStampDesignId
        get() = _selectedStampDesignId.asStateFlow()

    // 바텀시트 이벤트
    private val _sheetEventFlow = MutableSharedFlow<SheetEvent>()
    val sheetEventFlow = _sheetEventFlow.asSharedFlow()

    fun emitSheetEvent(event: SheetEvent) = viewModelScope.launch {
        _sheetEventFlow.emit(event)
    }

    /**
     * 도장 요청 거절하기
     */
    fun rejectMissionRequest(
        token: String,
        missionRequestId: Int,
        onComplete: (exception: Throwable?) -> Unit
    ) = viewModelScope.launch {
        repository
            .rejectMissionRequest(
                accessToken = token,
                missionRequestId = missionRequestId
            )
            .onSuccess {
                _missionList.removeIf {
                    (it as MissionRequestModel).id == missionRequestId
                }
                onComplete(null)
            }
            .onError { exception, _ ->
                exception.printStackTrace()
                onComplete(exception)
            }
    }

    fun setMissionId(id: Int) {
        _selectedMissionId.value = id
    }

    fun setStampDesignId(id: Int) {
        _selectedStampDesignId.value = id
    }

    // ----------------------------------------------------------------------------
    // 생성하는 곳에서 넘겨줄 파라미터와 hilt로 주입받는 파라미터를
    // 생성자에서 한번에 받을 수 있게 해주는 AssistedFactory
    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(
            missionList: List<MissionData>
            /*missionList: List<MissionModel>,
            missionRequestList: List<MissionRequestModel>*/
        ): MakeStampViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            missionList: List<MissionData>
            /*missionList: List<MissionModel>,
            missionRequestList: List<MissionRequestModel>*/
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(missionList) as T
            }
        }
    }
}