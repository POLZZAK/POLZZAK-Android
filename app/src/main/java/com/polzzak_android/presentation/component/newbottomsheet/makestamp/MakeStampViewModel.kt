package com.polzzak_android.presentation.component.newbottomsheet.makestamp

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.polzzak_android.data.repository.PointRepository
import com.polzzak_android.data.repository.StampBoardRepository
import com.polzzak_android.data.repository.UserRepository
import com.polzzak_android.presentation.component.newbottomsheet.base.SheetEvent
import com.polzzak_android.presentation.feature.stamp.model.MissionData
import com.polzzak_android.presentation.feature.stamp.model.MissionModel
import com.polzzak_android.presentation.feature.stamp.model.MissionRequestModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

// TODO: 리스트를 하나로 받기
class MakeStampViewModel @AssistedInject constructor(
    @Assisted val missionList: List<MissionData>
    /*@Assisted val missionList: List<MissionModel>,
    @Assisted val missionRequestList: List<MissionRequestModel>,*/
) : ViewModel() {

    init {
        Timber.d(">> created")
    }

    // TODO: 리스트 하나로 합치면 이거 어떻게 리턴?
    val isRequested: Boolean
        get() = missionList.firstOrNull() is MissionRequestModel

    // 미션 요청 아이디 =/= 미션 아이디
    // 구분하지 않고 저장 후 api 쏠때 구분
    var selectedMissionId: Int = -1

    private val _sheetEventFlow = MutableSharedFlow<SheetEvent>()
    val sheetEventFlow = _sheetEventFlow.asSharedFlow()

    fun emitSheetEvent(event: SheetEvent) = viewModelScope.launch {
        _sheetEventFlow.emit(event)
    }

    fun rejectMissionRequest(
        token: String,
        missionRequestId: Int
    ) {
        TODO()
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