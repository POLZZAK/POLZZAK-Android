package com.polzzak_android.presentation.feature.coupon.main.content

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentCouponContentBinding
import com.polzzak_android.presentation.common.adapter.MainCouponAdapter
import com.polzzak_android.presentation.common.adapter.MainCouponPagerAdapter
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.feature.coupon.model.Coupon
import com.polzzak_android.presentation.feature.coupon.model.CouponModel
import com.polzzak_android.presentation.feature.coupon.model.CouponPartner

class CouponContentFragment(private val state: String) : BaseFragment<FragmentCouponContentBinding>(), CouponContainerInteraction {
    override val layoutResId: Int = R.layout.fragment_coupon_content

    private lateinit var rvAdapter: MainCouponAdapter
    private lateinit var vpAdapter: MainCouponPagerAdapter

    companion object {
        @JvmStatic
        fun getInstance(state: String): CouponContentFragment {
            return CouponContentFragment(state)
        }
    }

    override fun initView() {
        super.initView()

        setAdapter()
    }

    private fun setAdapter() {
        val dummy = listOf<Coupon>(
            Coupon(
                type = 2,
                CouponPartner(
                    isKid = false,
                    memberId = 2,
                    memberType = "ECT",
                    nickname = " 연동o도장판o",
                ),
                listOf<CouponModel>(
                    CouponModel(
                        id = 10,
                        name = "도장판 이름1",
                        dDay = "보상1",
                        deadLine = "test"
                    ),
                    CouponModel(
                        id = 10,
                        name = "도장판 이름2",
                        dDay = "보상2",
                        deadLine = "test"
                    ),
                ),
            ),
            Coupon(
                type = 1,
                CouponPartner(
                    isKid = false,
                    memberId = 1,
                    memberType = "ECT",
                    nickname = "연동o도장판x11",
                ),
                listOf<CouponModel>(

                ),
            )
        )

        rvAdapter = MainCouponAdapter(dummy, this)
        binding.couponListRc.adapter = rvAdapter
        binding.couponListRc.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun initObserver() {
        super.initObserver()
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