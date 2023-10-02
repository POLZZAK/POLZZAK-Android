package com.polzzak_android.presentation.common.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.polzzak_android.R
import com.polzzak_android.databinding.ItemHasCouponBinding
import com.polzzak_android.databinding.ItemHasNotCouponBinding
import com.polzzak_android.presentation.feature.coupon.main.content.CouponContainerInteraction
import com.polzzak_android.presentation.feature.coupon.model.Coupon

class MainCouponAdapter(private var couponList: List<Coupon>, private val interaction: CouponContainerInteraction) :
    ListAdapter<Coupon, RecyclerView.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> {
                val view =
                    ItemHasNotCouponBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                NonViewHolder(view)
            }
            else -> {
                val view =
                    ItemHasCouponBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                YesViewHolder(view, interaction)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val curItem = couponList[position]

        when (curItem.type) {
            1 -> {
                (holder as NonViewHolder).bind(curItem)
            }

            else -> {
                (holder as YesViewHolder).bind(curItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return couponList.size
    }

    override fun getItemViewType(position: Int): Int {
        return couponList[position].type
    }

    fun setCouponList(newList: List<Coupon>) {
        couponList = newList
        notifyDataSetChanged()
    }

    inner class NonViewHolder(private val binding: ItemHasNotCouponBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Coupon) {
            with(binding) {
                // 아이가 아니라면 보호자 타입 라벨 표시
                if (item.partner.isKid.not()) {
                    prefix.setText(R.string.coupon_main_from)
                    memberType.text = item.partner.memberType
                    memberType.visibility = View.VISIBLE
                }

                userNickName.text = item.partner.nickname
            }
        }
    }

    inner class YesViewHolder(private val binding: ItemHasCouponBinding, interaction: CouponContainerInteraction) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            // transform
            val currentVisibleItemPx = 50

            binding.couponPager.addItemDecoration(object: RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    outRect.right = currentVisibleItemPx
                    outRect.left = currentVisibleItemPx
                }
            })

            val nextVisibleItemPx = 20
            val pageTranslationX = nextVisibleItemPx + currentVisibleItemPx

            binding.couponPager.offscreenPageLimit = 1

            binding.couponPager.setPageTransformer { page, position ->
                page.translationX = -pageTranslationX * (position)
            }
        }

        fun bind(item: Coupon) {
            with(binding) {
                // 아이가 아니라면 보호자 타입 라벨 표시
                if (item.partner.isKid.not()) {
                    prefix.setText(R.string.coupon_main_from)
                    memberType.text = item.partner.memberType
                    memberType.visibility = View.VISIBLE
                }

                userNickName.text = item.partner.nickname
                interaction.setViewPager(couponPager, curPage, totalPage, item.couponList)
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Coupon>() {
            override fun areItemsTheSame(oldItem: Coupon, newItem: Coupon): Boolean {
                return oldItem.partner.memberId == newItem.partner.memberId
            }

            override fun areContentsTheSame(oldItem: Coupon, newItem: Coupon): Boolean {
                return oldItem == newItem
            }
        }
    }


}
