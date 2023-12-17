package com.polzzak_android.presentation.component.newbottomsheet.makestamp

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.polzzak_android.R
import com.polzzak_android.databinding.BottomsheetFragmentChoiceMissionBinding
import com.polzzak_android.presentation.common.util.getAccessTokenOrNull
import com.polzzak_android.presentation.common.util.grandParentFragment
import com.polzzak_android.presentation.component.PolzzakSnackBar
import com.polzzak_android.presentation.component.errorOf
import com.polzzak_android.presentation.component.newbottomsheet.base.BaseSheetFragment
import com.polzzak_android.presentation.component.newbottomsheet.base.SheetEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ChoiceMissionSheet : BaseSheetFragment<BottomsheetFragmentChoiceMissionBinding>() {
    override val layoutId: Int
        get() = R.layout.bottomsheet_fragment_choice_mission

    private val viewModel: MakeStampViewModel by viewModels({ grandParentFragment })

    private val adapter: MissionListAdapter by lazy {
        MissionListAdapter (
            onClick = viewModel::setMissionId,
            onRejectClick = this::onRejectClick
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

        // 미션 리스트를 그대로 넘기면 같은 참조라서 화면갱신 안됨
        adapter.submitList(viewModel.missionList.toMutableList()) {
            // 거절하기를 누르면 미션 뷰홀더가 사라지면서
            // recyclerView의 height가 줄어드는 현상을 방지하기 위해
            // 최초의 height 값을 minHeight로 지정해줌
            binding.rvMissionList.also {
                it.post {
                    it.updateLayoutParams<ConstraintLayout.LayoutParams> {
                        matchConstraintMinHeight = it.measuredHeight
                    }
                }
            }
        }

        observe()
    }

    private fun observe() = lifecycleScope.launch {
        viewModel.selectedMissionId.collectLatest {
            Timber.d(">> selectedMissionId = $it")
            binding.btnNext.isEnabled = (it != -1)
        }
    }

    /**
     * 도장 요청 선택에서 거절하기 눌렀을 때
     */
    private fun onRejectClick(id: Int) {
        Timber.d(">> reject = $id")

        viewModel.rejectMissionRequest(
            token = getAccessTokenOrNull() ?: "",
            missionRequestId = id,
            onComplete = { exception ->
                if (exception != null) {
                    PolzzakSnackBar.errorOf(binding.root, exception).show()
                } else {
                    // 미션 리스트를 그대로 넘기면 같은 참조라서 화면갱신 안됨
                    adapter.submitList(viewModel.missionList.toMutableList()) {
                        // 리스트가 비면 안내문구 표시
                        binding.rvMissionList.postDelayed(
                            {
                                binding.tvEmptyText.visibility = if (adapter.itemCount == 0) {
                                    View.VISIBLE
                                } else {
                                    View.INVISIBLE
                                }
                            },
                            250
                        )
                    }
                }
            }
        )
    }
}