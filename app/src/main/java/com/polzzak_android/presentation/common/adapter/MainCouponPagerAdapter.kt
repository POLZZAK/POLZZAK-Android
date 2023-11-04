package com.polzzak_android.presentation.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.polzzak_android.R
import com.polzzak_android.databinding.ItemCouponBinding
import com.polzzak_android.presentation.common.util.SpannableBuilder
import com.polzzak_android.presentation.feature.coupon.main.content.CouponContainerInteraction
import com.polzzak_android.presentation.feature.coupon.model.CouponModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainCouponPagerAdapter(
    private var couponList: List<CouponModel>,
    private val interaction: CouponContainerInteraction
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


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
        private var isKid = binding.isKid
        private val giftRequest = binding.couponGiftRequest
        private val giftFinish = binding.couponGiftFinish

        private val deadlineTextFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd까지 주기로 약속했어요")

        fun bind(item: CouponModel) {
            isKid = item.isKid
            dDay.text = "⏰ D-" + item.dDay
            reward.text = item.name
            deadline.text = deadlineFormat(item.deadLine)

            container.setOnClickListener {
                interaction.onCouponPagerClicked(item)
            }

            giftRequest.setOnClickListener {
                interaction.onGiftRequestClicked(item)
            }

            giftFinish.setOnClickListener {
                interaction.onGiftFinishClicked(item)
            }
        }

        private fun deadlineFormat(deadline: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")
            val targetDate = inputFormat.parse(deadline)

            val outputFormat = SimpleDateFormat("yyyy.MM.dd까지 주기로 약속했어요")
            val outputSting = outputFormat.format(targetDate)

            val spannableString = SpannableBuilder.build(context = this.container.context) {
                span(
                    text = outputSting.toString().substring(0,10),
                    textColor = R.color.primary,
                    style = R.style.caption_12_500
                )
                span(
                    text = outputSting.toString().substring(10),
                    textColor = R.color.gray_800,
                    style = R.style.caption_12_500
                )
            }

            return spannableString.toString()
        }
    }

}