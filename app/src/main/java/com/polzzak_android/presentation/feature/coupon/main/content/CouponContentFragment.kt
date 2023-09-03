package com.polzzak_android.presentation.feature.coupon.main.content

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentCouponContentBinding
import com.polzzak_android.presentation.common.adapter.MainCouponAdapter
import com.polzzak_android.presentation.common.adapter.MainCouponPagerAdapter
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.model.ButtonCount
import com.polzzak_android.presentation.common.model.CommonButtonModel
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.util.getAccessTokenOrNull
import com.polzzak_android.presentation.component.bottomsheet.BottomSheetType
import com.polzzak_android.presentation.component.bottomsheet.CommonBottomSheetHelper
import com.polzzak_android.presentation.component.bottomsheet.CommonBottomSheetModel
import com.polzzak_android.presentation.component.bottomsheet.model.SelectUserStampBoardModel
import com.polzzak_android.presentation.component.bottomsheet.model.toSelectUserStampBoardModel
import com.polzzak_android.presentation.component.dialog.OnButtonClickListener
import com.polzzak_android.presentation.feature.coupon.main.protector.CouponViewModel
import com.polzzak_android.presentation.feature.coupon.model.Coupon
import com.polzzak_android.presentation.feature.coupon.model.CouponModel
import com.polzzak_android.presentation.feature.coupon.model.CouponPartner
import com.polzzak_android.presentation.feature.stamp.main.protector.StampLinkedUserViewModel

class CouponContentFragment : BaseFragment<FragmentCouponContentBinding>(), CouponContainerInteraction {
    override val layoutResId: Int = R.layout.fragment_coupon_content

    private lateinit var rvAdapter: MainCouponAdapter
    private lateinit var vpAdapter: MainCouponPagerAdapter

    private val linkedUserViewModel: StampLinkedUserViewModel by activityViewModels()
    private val couponViewModel: CouponViewModel by activityViewModels()

    private lateinit var state: String

    companion object {
        private const val ARG_STATE = "state"

        @JvmStatic
        fun getInstance(state: String): CouponContentFragment {
            val fragment = CouponContentFragment()
            val args = Bundle()
            args.putString(ARG_STATE, state)
            fragment.arguments = args
            return fragment
        }
    }

    override fun initView() {
        state = arguments?.getString(ARG_STATE).toString()
        super.initView()

        binding.fragment = this
        setAdapter()

        linkedUserViewModel.requestLinkedUserList(accessToken = getAccessTokenOrNull() ?: "")
    }

    private fun setAdapter() {
        binding.couponListRc.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun initObserver() {
        super.initObserver()
        // 연동된 사용자 있는지 확인
        linkedUserViewModel.linkedUserList.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ModelState.Success -> {
                    val hasLinkedUser = linkedUserViewModel.hasLinkedUser
                    binding.hasLinkedUser = hasLinkedUser

                    if (hasLinkedUser) {
                        // 쿠폰 조회
                        couponViewModel.requestCouponList(
                            token = getAccessTokenOrNull() ?: "",
                            couponState = this.state,
                            memberId = null
                        )
                    }
                }

                is ModelState.Error -> {
                    // todo: 에러 페이지
                }

                is ModelState.Loading -> {
                    // todo: 스켈레톤
                }
            }
        }

        // 쿠폰 조회
        couponViewModel.couponList.observe(viewLifecycleOwner) { state ->
            when(state) {
                is ModelState.Success -> {
                    val data = state.data
                    rvAdapter = MainCouponAdapter(data, this)
                    binding.couponListRc.adapter = rvAdapter
                }

                is ModelState.Error -> {
                    // todo: 에러
                }

                is ModelState.Loading -> {
                    // todo: 로딩
                }
            }
        }
    }

    fun clickUserFilter() {
        val userList = linkedUserViewModel.getLinkedUserList()

        if (userList != null) {
            CommonBottomSheetHelper.getInstance(
                data = CommonBottomSheetModel(
                    type = BottomSheetType.SELECT_STAMP_BOARD,
                    title = "누구에게 선물한 쿠폰을 볼까요?",
                    contentList = userList.map { it.toSelectUserStampBoardModel() },
                    button = CommonButtonModel(
                        buttonCount = ButtonCount.ZERO
                    )
                ),
                onClickListener = {
                    object : OnButtonClickListener {
                        override fun setBusinessLogic() {}

                        override fun getReturnValue(value: Any) {
                            val selectedUserInfo = (value as SelectUserStampBoardModel)
                            binding.selectContainer.visibility = View.GONE

                            couponViewModel.requestCouponList(
                                token = getAccessTokenOrNull() ?: "",
                                couponState = state,
                                memberId = selectedUserInfo.userId
                            )
                        }

                    }
                }
            ).show(childFragmentManager, null)
        }
    }

    override fun setViewPager(
        view: ViewPager2,
        curInd: TextView,
        totalInd: TextView,
        stampList: List<CouponModel>
    ) {
        // adapter
        vpAdapter = MainCouponPagerAdapter(stampList, this)
        view.adapter = vpAdapter

        // indicator
        // Todo: 임시
        curInd.text = "1"
        totalInd.text = resources.getString(R.string.viewpager_indicator, stampList.size)
        view.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                curInd.text = position.plus(1).toString()
                curInd.invalidate()
            }
        })
    }

    override fun onCouponPagerClicked(couponModel: CouponModel) {
        // Todo: 임시
        Toast.makeText(context, "${couponModel.name} 클릭", Toast.LENGTH_SHORT).show()

        // TODO: 부모 nav_graph.xml에도 같은 action 추가 필요
        findNavController().navigate(
            R.id.action_to_stampBoardDetailFragment,
            Bundle().apply { putInt("boardId", couponModel.id) }
        )
    }

}