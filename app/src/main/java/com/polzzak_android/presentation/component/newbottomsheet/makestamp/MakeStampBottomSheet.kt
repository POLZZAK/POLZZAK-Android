package com.polzzak_android.presentation.component.newbottomsheet.makestamp

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.polzzak_android.R
import com.polzzak_android.presentation.component.newbottomsheet.base.BaseNavigationBottomSheet
import com.polzzak_android.presentation.component.newbottomsheet.base.SheetEvent
import com.polzzak_android.presentation.feature.stamp.model.MissionData
import com.polzzak_android.presentation.feature.stamp.model.MissionModel
import com.polzzak_android.presentation.feature.stamp.model.MissionRequestModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MakeStampBottomSheet(
    missionList: List<MissionData>,
    private val onMakeStampClick: (missionId: Int, stampDesignId: Int) -> Unit
    /*missionList: List<MissionModel> = emptyList(),
    missionRequestList: List<MissionRequestModel> = emptyList()*/
) : BaseNavigationBottomSheet() {
    override val navGraphId: Int
        get() = R.navigation.bottomsheet_make_stamp_nav_graph

    @Inject
    lateinit var viewModelFactory: MakeStampViewModel.AssistedFactory

    private val viewModel: MakeStampViewModel by viewModels {
        MakeStampViewModel.provideFactory(viewModelFactory, missionList)
    }

    override fun initialize() {
        lifecycleScope.launch {
            // repeatOnLifeCycle을 STARTED로 지정해서 사용하면
            // 바텀시트에 나타날 Fragment보다 늦게 viewModel이 초기화됨
            // -> Fragment에서 바텀시트의 viewModel 인스턴스를 가져올 수 없음
            viewModel.sheetEventFlow.collectLatest {
                when (it) {
                    SheetEvent.NEXT -> navController.navigate(R.id.action_choiceMissionSheet_to_choiceStampSheet)
                    SheetEvent.PREV -> navController.popBackStack()
                    SheetEvent.CLOSE -> dialog?.dismiss()
                    SheetEvent.ACTION -> {
                        dialog?.dismiss()
                        onMakeStampClick(
                            viewModel.selectedMissionId.value,
                            viewModel.selectedStampDesignId.value
                        )
                    }
                }
            }
        }
    }
}