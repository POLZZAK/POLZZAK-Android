package com.polzzak_android.presentation.makingStamp

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentMakeStampBinding
import com.polzzak_android.presentation.adapter.MakeStampCountAdapter
import com.polzzak_android.presentation.adapter.MakeStampMissionAdapter
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.base.ToolbarIconInteraction
import com.polzzak_android.presentation.common.model.ButtonCount
import com.polzzak_android.presentation.common.model.CommonButtonModel
import com.polzzak_android.presentation.common.model.CommonDialogContent
import com.polzzak_android.presentation.common.model.CommonDialogModel
import com.polzzak_android.presentation.common.model.DialogStyleType
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.model.ToolbarData
import com.polzzak_android.presentation.common.widget.CommonDialogHelper
import com.polzzak_android.presentation.common.widget.OnButtonClickListener
import com.polzzak_android.presentation.common.widget.ToolbarHelper
import com.polzzak_android.presentation.makingStamp.intreraction.MissionInteraction
import com.polzzak_android.presentation.makingStamp.intreraction.StampCountInteraction
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MakeStampFragment : BaseFragment<FragmentMakeStampBinding>(), StampCountInteraction,
    MissionInteraction, ToolbarIconInteraction {
    override val layoutResId: Int = R.layout.fragment_make_stamp

    private lateinit var makeStampCountAdapter: MakeStampCountAdapter
    private lateinit var stampCountSelectHelper: StampCountSelectedHelper
    private lateinit var stampMissionAdapter: MakeStampMissionAdapter

    private val makeStampViewModel: MakeStampViewModel by activityViewModels()


    override fun setToolbar() {
        super.setToolbar()

        ToolbarHelper(
            data = ToolbarData(
                popStack = findNavController(),
                titleText = "도장판 생성",
                iconText = "등록",
                iconInteraction = this
            ),
            backButtonView =binding.toolbar.toolbarBackButton,
            titleView = binding.toolbar.toolbarTitle,
            textIconView = binding.toolbar.toolbarTextIcon,
        ).set()
    }

    private val requestStampDialog = CommonDialogHelper.getInstance(
        content = CommonDialogModel(
            type = DialogStyleType.ALERT,
            content = CommonDialogContent(
                title = "도장판을 등록하시겠어요?",
            ),
            button = CommonButtonModel(
                buttonCount = ButtonCount.TWO,
                negativeButtonText = "아니요",
                positiveButtonText = "네, 등록할게요"
            )
        ),
        onConfirmListener = {
            object : OnButtonClickListener {
                override fun setBusinessLogic() {
                    makeStampViewModel.setStampBoardName(binding.stampBoardName.text.toString())
                    makeStampViewModel.setStampBoardReward(binding.stampBoardReward.text.toString())
                    makeStampViewModel.validateInput()
                }
                override fun getReturnValue(value: Any) {}
            }
        }
    )

    private val loadingStampDialog = CommonDialogHelper.getInstance(
        content = CommonDialogModel(
            type = DialogStyleType.LOADING,
            content = CommonDialogContent(
                title = "도장판이 곧 완성돼요"
            ),
            button = CommonButtonModel(
                buttonCount = ButtonCount.ZERO,
            )
        )
    )

    override fun initView() {
        super.initView()
        stampCountSelectHelper = StampCountSelectedHelper.getInstance()

        setAdapter()
        initData()

        // todo: 닉네임 임시
        binding.selectedKidName.text = "임시닉네임"

        binding.missionEnrollButton.setOnClickListener {
            makeStampViewModel.createMission()
        }

        binding.missionExButton.setOnClickListener {
            findNavController().navigate(R.id.action_makeStampFragment_to_exampleMissionFragment)
        }
    }

    private fun setAdapter() {
        // 도장판 개수
        makeStampCountAdapter =
            MakeStampCountAdapter(binding.stampCountRc, this, stampCountSelectHelper)
        binding.stampCountRc.adapter = makeStampCountAdapter

        // 미션
        stampMissionAdapter = MakeStampMissionAdapter(this)
        binding.missionRc.adapter = stampMissionAdapter
    }

    override fun initObserver() {
        super.initObserver()
        observeValidateResult()

        // 미션 리스트
        makeStampViewModel.missionList.observe(this) { missionList ->
            stampMissionAdapter.submitList(missionList.missionList)
            stampMissionAdapter.validate(missionList)
        }

        // 미션 리스트 카운트
        makeStampViewModel.missionListSize.observe(this) { missionListSize ->
            stampMissionAdapter.setMissionListSize(missionListSize)

            if (missionListSize < 50) {
                binding.missionEnrollButton.apply {
                    isEnabled = true
                    text = "+ 미션 추가"
                }
            } else {
                binding.missionEnrollButton.apply {
                    isEnabled = false
                    text = "미션은 50개까지 만들 수 있어요"
                }
            }
        }

        // 미션 등록
        makeStampViewModel.makeStampBoardState.observe(this) { modelState ->
            when (modelState) {
                is ModelState.Loading -> {
                    loadingStampDialog.show(childFragmentManager, null)
                }
                is ModelState.Success -> {
                    loadingStampDialog.dismiss()
                }
                is ModelState.Error -> {
                    val message = modelState.exception
                    loadingStampDialog.dismiss()
                    Toast.makeText(context, "실패: $message", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observeValidateResult() {
        // 이름
        makeStampViewModel.stampBoardName.observe(this) { name ->
            if (name.isValidate) {
                binding.stampBoardName.setBackgroundResource(R.drawable.bg_gray_stroke_white_bg_r8)
                binding.stampBoardNameError.visibility = View.GONE
            } else {
                binding.stampBoardName.setBackgroundResource(R.drawable.bg_red_stroke_white_bg_r8)
                binding.stampBoardNameError.apply {
                    visibility = View.VISIBLE
                    text = name.errorMessage
                }
            }
        }

        // 보상
        makeStampViewModel.stampBoardReward.observe(this) { reward ->
            if (reward.isValidate) {
                binding.stampBoardReward.setBackgroundResource(R.drawable.bg_gray_stroke_white_bg_r8)
                binding.stampBoardRewardError.visibility = View.GONE
            } else {
                binding.stampBoardReward.setBackgroundResource(R.drawable.bg_red_stroke_white_bg_r8)
                binding.stampBoardRewardError.apply {
                    visibility = View.VISIBLE
                    text = reward.errorMessage
                }
            }
        }

        // 도장 개수
        makeStampViewModel.stampCount.observe(this) { count ->
            if (count.isValidate) {
                binding.stampCountRc.setBackgroundResource(R.drawable.bg_gray_stroke_white_bg_r8)
                binding.stampBoardCountError.visibility = View.GONE
            } else {
                binding.stampCountRc.setBackgroundResource(R.drawable.bg_red_stroke_white_bg_r8)
                binding.stampBoardCountError.apply {
                    visibility = View.VISIBLE
                    text = count.errorMessage
                }
            }
        }
    }

    private fun initData() {
        // 도장판 개수
        makeStampCountAdapter.submitList(listOf(10, 12, 16, 20, 25, 30, 36, 40, 48, 60))        // todo: 하드코딩 바꾸기?
        stampCountSelectHelper.stampCount = makeStampViewModel.getStampCountList()
        makeStampViewModel.setMissionListSize(makeStampViewModel.getMissionListSize())

        // 뷰모델 데이터 초기화 todo: 수정 진입 시 변경 예정
//        makeStampViewModel.initData()
    }

    override fun onStampCountClicked(view: TextView, value: Int) {
        val isNewCount = stampCountSelectHelper.onCountClicked(view = view, value = value)

        if (isNewCount) {
            makeStampViewModel.setStampCount(input = value)
        }
    }

    override fun onDeletedMissionIconClicked(mission: String, view: ImageButton) {
        makeStampViewModel.deleteMission(mission)
    }

    override fun updateMissionList(missionList: List<String>) {
        makeStampViewModel.updateMissionList(missionList)
    }

    override fun onToolbarIconClicked() {
        // 도장판 등록
        requestStampDialog.show(childFragmentManager, "Dialog")
    }

}