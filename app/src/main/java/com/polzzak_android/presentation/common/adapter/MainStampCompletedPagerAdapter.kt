package com.polzzak_android.presentation.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.polzzak_android.databinding.ItemStampCompletedBinding
import com.polzzak_android.databinding.ItemStampProgressBinding
import com.polzzak_android.presentation.feature.stamp.intercation.MainProgressInteraction
import com.polzzak_android.presentation.feature.stamp.model.StampBoardSummaryModel

class MainStampCompletedPagerAdapter(
    private var stampList: List<StampBoardSummaryModel>?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemStampCompletedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return stampList?.size ?: 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val curItem = stampList?.get(position)

        if (curItem != null) {
            (holder as ViewHolder).bind(curItem)
        }
    }

    fun setCompletedStampList(newList: List<StampBoardSummaryModel>) {
        stampList = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: ItemStampCompletedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val userHeaderTxt = binding.stampNickName
        private val reward = binding.rewardContent

        fun bind(item: StampBoardSummaryModel) {
            userHeaderTxt.text = item.name
            reward.text = item.reward
        }
    }

}