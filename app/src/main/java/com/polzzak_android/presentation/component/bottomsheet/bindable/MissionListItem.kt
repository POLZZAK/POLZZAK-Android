package com.polzzak_android.presentation.component.bottomsheet.bindable

import android.view.View
import com.polzzak_android.R
import com.polzzak_android.databinding.ItemMissionBinding
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.feature.stamp.model.MissionModel

class MissionListItem(
    val model: MissionModel,
    private val interaction: BottomSheetMissionListClickInteraction
) :
    BindableItem<ItemMissionBinding>() {
    override val layoutRes = R.layout.item_mission

    var isSelected: Boolean = false

    override fun bind(binding: ItemMissionBinding, position: Int) {
        with(binding) {
            data = model.content

            // isSelected에 따른 view와 value 세팅
            root.isSelected = isSelected
            if (isSelected) {
                itemExMissionText.setTextColor(root.context.getColor(R.color.primary))
                itemExMissionCheck.visibility = View.VISIBLE
            } else {
                itemExMissionText.setTextColor(root.context.getColor(R.color.black))
                itemExMissionCheck.visibility = View.GONE
            }

            root.setOnClickListener {
                isSelected = !isSelected
                it.isSelected = isSelected

                if (isSelected) {
                    binding.itemExMissionText.setTextColor(it.context.getColor(R.color.primary))
                    itemExMissionCheck.visibility = View.VISIBLE
                } else {
                    binding.itemExMissionText.setTextColor(it.context.getColor(R.color.black))
                    itemExMissionCheck.visibility = View.GONE
                }

                interaction.onMissionClick(model)
            }
        }
    }

    override fun areItemsTheSame(other: BindableItem<*>): Boolean =
        other is MissionListItem && other.model.id == this.model.id


    override fun areContentsTheSame(other: BindableItem<*>): Boolean =
        other is MissionListItem && other.model == this.model
}

interface BottomSheetMissionListClickInteraction {
    fun onMissionClick(model: MissionModel)
}