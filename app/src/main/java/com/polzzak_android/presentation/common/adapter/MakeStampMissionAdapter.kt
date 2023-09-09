package com.polzzak_android.presentation.common.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.polzzak_android.R
import com.polzzak_android.databinding.ItemStampMissionBinding
import com.polzzak_android.presentation.feature.stamp.make.intreraction.MissionInteraction
import com.polzzak_android.presentation.feature.stamp.model.MakeStampMissionListModel
import com.polzzak_android.presentation.feature.stamp.model.MissionModel

class MakeStampMissionAdapter(
    private val interaction: MissionInteraction,
) :
    ListAdapter<String, MakeStampMissionAdapter.ViewHolder>(DiffCallback) {

    private var missionList: MutableList<String>? = null
    private var missionListSize: Int = 0
    private var errorMessage: String? = null
    private var errorPosition: Int? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            ItemStampMissionBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interaction)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
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

        fun bind(value: String, position: Int) {
            binding.data = value
            binding.missionListSize = missionListSize

            binding.itemMissionDelButton.setOnClickListener {
                interaction.onDeletedMissionIconClicked(
                    mission = value,
                    view = binding.itemMissionDelButton
                )
            }

            // 유효성 체크
            setValidateResult(position)
        }

        private fun setValidateResult(position: Int) {
            if (errorPosition != null && errorPosition == position) {
                binding.itemMissionInputContainer.setBackgroundResource(R.drawable.bg_red_stroke_white_bg_r8)
                binding.errorMessage = errorMessage
                binding.itemMissionErrorMessage.visibility = View.VISIBLE
            } else {
                binding.itemMissionInputContainer.setBackgroundResource(R.drawable.bg_gray_stroke_white_bg_r8)
                binding.itemMissionErrorMessage.visibility = View.GONE
            }
        }
    }

    override fun submitList(list: MutableList<String>?) {
        super.submitList(list)
        missionList = list
        notifyDataSetChanged()
    }

    fun setMissionListSize(size: Int) {
        missionListSize = size
        notifyDataSetChanged()
    }

    fun addMission(list: MutableList<MissionModel>) {
        missionList?.addAll(list.map { data -> data.content })
        notifyDataSetChanged()
    }

    fun validate(model: MakeStampMissionListModel) {
        if (model.isValidate) {
            errorMessage = null
            errorPosition = null
        } else {
            errorMessage = model.errorMessage
            errorPosition = model.errorPosition
        }
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