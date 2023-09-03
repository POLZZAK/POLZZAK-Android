package com.polzzak_android.presentation.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.polzzak_android.databinding.ItemStampProgressBinding
import com.polzzak_android.presentation.feature.stamp.intercation.MainProgressInteraction
import com.polzzak_android.presentation.feature.stamp.model.StampBoardSummaryModel

class MainStampPagerAdapter(
    private val dummy: List<StampBoardSummaryModel>?,
    private val interaction: MainProgressInteraction
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var stampList = dummy
    //private var stampList: listOf<StampBoardSummary>() = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemStampProgressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view, interaction)
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

    fun setStampList(newList: List<StampBoardSummaryModel>) {
        stampList = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: ItemStampProgressBinding, interaction: MainProgressInteraction) :
        RecyclerView.ViewHolder(binding.root) {
        private val container = binding.stampContainer

        private val userHeaderTxt = binding.stampNickName
        private val progressView = binding.stampProgress
        private val curCnt = binding.stampCurCnt
        private val totalCnt = binding.stampTotalCnt
        private val reward = binding.rawardContent
        private val stampReq = binding.stampReqTimes

        fun bind(item: StampBoardSummaryModel) {
            userHeaderTxt.text = item.name
            curCnt.text = item.currentStampCount.toString()
            totalCnt.text = item.goalStampCount.toString()
            reward.text = item.reward
            stampReq.text = item.missionRequestCount.toString()

            interaction.setProgressAnim(
                curCnt = item.currentStampCount,
                totalCnt = item.goalStampCount,
                view = progressView
            )

            container.setOnClickListener {
                interaction.onStampPagerClicked(item)
            }
        }
    }

}