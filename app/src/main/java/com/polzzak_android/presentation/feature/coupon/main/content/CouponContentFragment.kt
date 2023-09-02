package com.polzzak_android.presentation.feature.coupon.main.content

import android.os.Bundle
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
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.util.getAccessTokenOrNull
import com.polzzak_android.presentation.feature.coupon.main.protector.CouponViewModel
import com.polzzak_android.presentation.feature.coupon.model.Coupon
import com.polzzak_android.presentation.feature.coupon.model.CouponModel
import com.polzzak_android.presentation.feature.coupon.model.CouponPartner

class CouponContentFragment(private val state: String) : BaseFragment<FragmentCouponContentBinding>(), CouponContainerInteraction {
    override val layoutResId: Int = R.layout.fragment_coupon_content

    private lateinit var rvAdapter: MainCouponAdapter
    private lateinit var vpAdapter: MainCouponPagerAdapter

    private val couponViewModel: CouponViewModel by activityViewModels()

    companion object {
        @JvmStatic
        fun getInstance(state: String): CouponContentFragment {
            return CouponContentFragment(state)
        }
    }

    override fun initView() {
        super.initView()

        setAdapter()

        couponViewModel.requestCouponList(
            token = getAccessTokenOrNull() ?: "",
            couponState = state,
            memberId = null
        )
    }

    private fun setAdapter() {
        binding.couponListRc.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun initObserver() {
        super.initObserver()
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