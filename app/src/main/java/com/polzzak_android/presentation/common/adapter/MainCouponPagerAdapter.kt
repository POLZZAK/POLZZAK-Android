package com.polzzak_android.presentation.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.polzzak_android.databinding.ItemCouponBinding
import com.polzzak_android.presentation.feature.coupon.main.content.CouponContainerInteraction
import com.polzzak_android.presentation.feature.coupon.model.CouponModel

class MainCouponPagerAdapter(
    private var couponList: List<CouponModel>,
    private val interaction: CouponContainerInteraction
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemCouponBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view, interaction)
    }

    override fun getItemCount(): Int {
        return couponList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val curItem = couponList[position]

        (holder as ViewHolder).bind(curItem)
    }

    fun setCouponList(newList: List<CouponModel>) {
        couponList = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: ItemCouponBinding, interaction: CouponContainerInteraction) :
        RecyclerView.ViewHolder(binding.root) {
        private val container = binding.root

        private val dDay = binding.couponDDay
        private val reward = binding.couponName
        private val deadline = binding.couponDeadline

        fun bind(item: CouponModel) {
            dDay.text = ""          // todo: 계산식 필요
            reward.text = item.name
            deadline.text = item.deadLine   // todo: 변환 필요

            container.setOnClickListener {
                interaction.onCouponPagerClicked(item)
            }
        }
    }

}