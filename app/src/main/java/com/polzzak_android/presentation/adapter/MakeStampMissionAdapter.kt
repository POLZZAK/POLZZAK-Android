package com.polzzak_android.presentation.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.polzzak_android.databinding.ItemStampMissionBinding
import com.polzzak_android.presentation.makingStamp.MissionInteraction

class MakeStampMissionAdapter(
    private val interaction: MissionInteraction,
) :
    ListAdapter<String, MakeStampMissionAdapter.ViewHolder>(DiffCallback) {

    private var missionList: List<String>? = null
    private var missionListSize: Int = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MakeStampMissionAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            ItemStampMissionBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interaction)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemStampMissionBinding,
        private val interaction: MissionInteraction
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            // 사용자 입력 감지 리스너
            binding.itemMissionInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    /**
                     * 사용자의 키보드 입력에 따라 미션 text 데이터 업데이트
                     */
                    if (adapterPosition != RecyclerView.NO_POSITION && s != null) {
                        val updatedMissionList = missionList?.toMutableList() ?: mutableListOf()
                        updatedMissionList[adapterPosition] = s.toString()

                        missionList = updatedMissionList
                        interaction.updateMissionList(updatedMissionList)
                    }
                }
            })
        }

        fun bind(value: String) {
            binding.data = value
            binding.missionListSize = missionListSize

            binding.itemMissionDelButton.setOnClickListener {
                interaction.onDeletedIconClicked(
                    mission = value,
                    view = binding.itemMissionDelButton
                )
            }
        }
    }

    override fun submitList(list: List<String>?) {
        super.submitList(list)
        missionList = list
        notifyDataSetChanged()
    }

    fun setMissionListSize(size: Int) {
        missionListSize = size
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