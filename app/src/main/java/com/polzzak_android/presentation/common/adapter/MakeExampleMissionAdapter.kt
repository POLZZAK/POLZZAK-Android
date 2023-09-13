package com.polzzak_android.presentation.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.polzzak_android.databinding.ItemMissionBinding
import com.polzzak_android.presentation.feature.stamp.make.ExampleMissionHelper
import com.polzzak_android.presentation.feature.stamp.make.intreraction.ExampleMissionInteraction

@Deprecated("바텀 시트 사용")
class MakeExampleMissionAdapter(
    private val interaction: ExampleMissionInteraction,
    private val exampleMissionHelper: ExampleMissionHelper
    ) :
    ListAdapter<String, MakeExampleMissionAdapter.ViewHolder>(DiffCallback) {

    private var missionList: List<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            ItemMissionBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interaction)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemMissionBinding,
        private val interaction: ExampleMissionInteraction
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(value: String) {
            binding.data = value

            /**
             * recyclerview selected issue 막는 체크 로직
             * 체크 로직이 없으면 아이템 재활용 때문에 selected 상태가 됨
             */
//            val isSelected = exampleMissionHelper.checkSelectedMission(value)
//            exampleMissionHelper.setSelectedMissionView(binding, isSelected)

            binding.root.setOnClickListener {
                interaction.onExampleMissionClicked(binding, value)
            }
        }
    }

    override fun submitList(list: List<String>?) {
        super.submitList(list)
        missionList = list
        notifyDataSetChanged()
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }
}