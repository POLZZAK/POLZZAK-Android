package com.polzzak_android.presentation.component.newbottomsheet.makestamp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.polzzak_android.R
import com.polzzak_android.databinding.BottomsheetFragmentChoiceMissionBinding
import com.polzzak_android.databinding.ItemMissionBinding
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.common.util.getAccessTokenOrNull
import com.polzzak_android.presentation.common.util.grandParentFragment
import com.polzzak_android.presentation.component.newbottomsheet.base.BaseSheetFragment
import com.polzzak_android.presentation.component.newbottomsheet.base.SheetEvent
import com.polzzak_android.presentation.feature.stamp.model.MissionModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.properties.Delegates

@AndroidEntryPoint
class ChoiceMissionSheet : BaseSheetFragment<BottomsheetFragmentChoiceMissionBinding>() {
    override val layoutId: Int
        get() = R.layout.bottomsheet_fragment_choice_mission

    private val viewModel: MakeStampViewModel by viewModels({ grandParentFragment })

    private val adapter: MissionListAdapter by lazy {
        MissionListAdapter (
            onClick = viewModel::setMissionId,
            onRejectClick = {
                viewModel.rejectMissionRequest(
                    token = getAccessTokenOrNull() ?: "",
                    missionRequestId = it
                )
            }
        )
    }

    override fun initialize() {
        binding.apply {
            tvTitle.text = if (viewModel.isRequested) {
                "도장 요청 선택"
            } else {
                "미션 직접 선택"
            }

            btnNext.setOnClickListener {
                viewModel.emitSheetEvent(SheetEvent.NEXT)
            }

            btnClose.setOnClickListener {
                viewModel.emitSheetEvent(SheetEvent.CLOSE)
            }

            rvMissionList.adapter = adapter
        }

        adapter.submitList(viewModel.missionList)

        observe()
    }

    private fun observe() = lifecycleScope.launch {
        viewModel.selectedMissionId.collectLatest {
            Timber.d(">> selectedMissionId = $it")
            binding.btnNext.isEnabled = (it != -1)
        }
    }
}