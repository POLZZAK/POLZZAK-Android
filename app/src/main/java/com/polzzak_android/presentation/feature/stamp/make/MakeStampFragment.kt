package com.polzzak_android.presentation.feature.stamp.make

import android.graphics.Rect
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.toSpannable
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.polzzak_android.R
import com.polzzak_android.data.remote.model.response.UserInfoDto
import com.polzzak_android.databinding.FragmentMakeStampBinding
import com.polzzak_android.presentation.common.adapter.MakeStampCountAdapter
import com.polzzak_android.presentation.common.adapter.MakeStampMissionAdapter
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.component.toolbar.ToolbarIconInteraction
import com.polzzak_android.presentation.common.model.ButtonCount
import com.polzzak_android.presentation.common.model.CommonButtonModel
import com.polzzak_android.presentation.component.dialog.CommonDialogContent
import com.polzzak_android.presentation.component.dialog.CommonDialogModel
import com.polzzak_android.presentation.component.dialog.DialogStyleType
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.util.getAccessTokenOrNull
import com.polzzak_android.presentation.component.bottomsheet.BottomSheetType
import com.polzzak_android.presentation.component.bottomsheet.CommonBottomSheetHelper
import com.polzzak_android.presentation.component.bottomsheet.CommonBottomSheetModel
import com.polzzak_android.presentation.component.bottomsheet.model.SelectUserMakeBoardModelModel
import com.polzzak_android.presentation.component.toolbar.ToolbarData
import com.polzzak_android.presentation.component.dialog.CommonDialogHelper
import com.polzzak_android.presentation.component.dialog.OnButtonClickListener
import com.polzzak_android.presentation.component.toolbar.ToolbarHelper
import com.polzzak_android.presentation.feature.stamp.main.protector.StampLinkedUserViewModel
import com.polzzak_android.presentation.feature.stamp.make.intreraction.MissionInteraction
import com.polzzak_android.presentation.feature.stamp.make.intreraction.StampCountInteraction
import com.polzzak_android.presentation.feature.stamp.model.MissionModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MakeStampFragment : BaseFragment<FragmentMakeStampBinding>(), StampCountInteraction,
    MissionInteraction, ToolbarIconInteraction {
    override val layoutResId: Int = R.layout.fragment_make_stamp

    private lateinit var makeStampCountAdapter: MakeStampCountAdapter
    private lateinit var stampCountSelectHelper: StampCountSelectedHelper
    private lateinit var stampMissionAdapter: MakeStampMissionAdapter

    private val makeStampViewModel: MakeStampViewModel by activityViewModels()
    private val linkedUserViewModel: StampLinkedUserViewModel by activityViewModels()

    private lateinit var toolbarHelper: ToolbarHelper

    override fun setToolbar() {
        super.setToolbar()

        toolbarHelper = ToolbarHelper(
            data = ToolbarData(
                popStack = findNavController(),
                titleText = "도장판 생성",
                iconText = "등록",
                iconInteraction = this
            ),
            toolbar = binding.toolbar
        )

        toolbarHelper.set()
        toolbarHelper.updateBackButtonImage(R.drawable.ic_close)
        toolbarHelper.updateTextIconColor(R.color.primary)
    }

    private val requestStampDialog = CommonDialogHelper.getInstance(
        content = CommonDialogModel(
            type = DialogStyleType.ALERT,
            content = CommonDialogContent(
                title = "도장판을 등록하시겠어요?".toSpannable(),
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
                title = "도장판이 곧 완성돼요".toSpannable()
            ),
            button = CommonButtonModel(
                buttonCount = ButtonCount.ZERO,
            )
        )
    )

    override fun initView() {
        super.initView()
        stampCountSelectHelper = StampCountSelectedHelper.getInstance()

        binding.fragment = this

        setAdapter()
        initData()

        linkedUserViewModel.requestLinkedUserList(accessToken = getAccessTokenOrNull() ?: "")

        binding.missionEnrollButton.setOnClickListener {
            makeStampViewModel.createMission()
        }

        binding.missionExButton.setOnClickListener {
            val exampleList = ExampleMissionHelper().getList()
            CommonBottomSheetHelper.getInstance(
                data = CommonBottomSheetModel(
                    type = BottomSheetType.EXAMPLE_MISSION,
                    title = "마음에 드는 미션들을 추가해보세요!",
                    contentList = exampleList,
                    button = CommonButtonModel(
                        buttonCount = ButtonCount.ONE,
                        positiveButtonText = "추가하기"
                    )
                ),
                onClickListener = {
                    object : OnButtonClickListener {
                        override fun setBusinessLogic() {}

                        override fun getReturnValue(value: Any) {
                            val missionList = value as List<MissionModel>
                            stampMissionAdapter.addMission(missionList.toMutableList())
                        }

                    }
                }
            ).show(childFragmentManager, null)
        }
    }

    private fun setAdapter() {
        // 도장판 개수
        makeStampCountAdapter =
            MakeStampCountAdapter(binding.stampCountRc, this, stampCountSelectHelper)
        binding.stampCountRc.adapter = makeStampCountAdapter

        val layoutManager = GridLayoutManager(context, 5)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return 1
            }
        }
        binding.stampCountRc.layoutManager = layoutManager

        binding.stampCountRc.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val position = parent.getChildAdapterPosition(view)
                val itemCount = state.itemCount

                outRect.top = 10
                outRect.bottom = 10

                if (position >= itemCount - 2) {
                    outRect.right = 0
                } else {
                    outRect.right = 10
                }
            }
        })

        // 미션
        stampMissionAdapter = MakeStampMissionAdapter(this)
        binding.missionRc.adapter = stampMissionAdapter
    }

    override fun initObserver() {
        super.initObserver()
        observeValidateResult()

        // 연동된 사용자 있는지 확인
        linkedUserViewModel.linkedUserList.observe(viewLifecycleOwner) { state ->
            if (state is ModelState.Success) {
                val hasLinkedUser = linkedUserViewModel.hasLinkedUser
                val firstUser = state.data.first()

                if (hasLinkedUser) {
                    binding.selectedKidName.text = firstUser.nickName
                    Glide.with(requireContext()).load(firstUser.profileUrl)
                        .error(R.drawable.ic_launcher_background)
                        .into(binding.selectedKidProfileImg)
                }
            }
        }

        // 미션 리스트
        makeStampViewModel.missionList.observe(this) { data ->
            stampMissionAdapter.submitList(data.missionList.toMutableList())
            stampMissionAdapter.validate(data)
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
        makeStampCountAdapter.submitList(
            listOf(10, 12, 16, 20, 25, 30, 36, 40, 48, 60)
        )        // todo: 하드코딩 바꾸기?
        stampCountSelectHelper.stampCount = makeStampViewModel.getStampCountList()
        makeStampViewModel.setMissionListSize(makeStampViewModel.getMissionListSize())

        // 뷰모델 데이터 초기화 todo: 수정 진입 시 변경 예정
//        makeStampViewModel.initData()
    }

    fun clickKidSelector() {
        CommonBottomSheetHelper.getInstance(
            data = CommonBottomSheetModel(
                type = BottomSheetType.PROFILE_IMAGE,
                title = "도장판을 누구에게 만들어줄까요?",
                contentList = linkedUserViewModel.getLinkedUserList() ?: listOf(""),
                button = CommonButtonModel(
                    buttonCount = ButtonCount.ONE,
                    positiveButtonText = "선택 완료"
                )
            ),
            onClickListener = {
                object : OnButtonClickListener {
                    override fun setBusinessLogic() {}

                    override fun getReturnValue(value: Any) {
                        val selectedKid = value as UserInfoDto
                        with(binding) {
                            binding.selectedKidName.text = selectedKid.nickName
                            Glide.with(requireContext()).load(selectedKid.profileUrl)
                                .error(R.drawable.ic_launcher_background)
                                .into(binding.selectedKidProfileImg)
                        }
                    }

                }
            }
        ).show(childFragmentManager, null)
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