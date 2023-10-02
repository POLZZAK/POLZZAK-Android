package com.polzzak_android.presentation.feature.stamp.detail.protector.stampBottomSheet.bindable

import com.polzzak_android.R
import com.polzzak_android.databinding.ItemSelectingStampBinding
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.feature.stamp.detail.protector.stampBottomSheet.CompleteStampModel

class StampBottomSheetStampListItem(
        private val model: CompleteStampModel,
        private val interaction: BottomSheetStampListClickInteraction
) :
        BindableItem<ItemSelectingStampBinding>() {
    override val layoutRes = R.layout.item_selecting_stamp

    var isSelected: Boolean = false

    override fun bind(binding: ItemSelectingStampBinding, position: Int) {
        when (model.id) {
            1 -> binding.stamp.setImageResource(R.drawable.ic_stamp_1)
            2 -> binding.stamp.setImageResource(R.drawable.ic_stamp_2)
            3 -> binding.stamp.setImageResource(R.drawable.ic_stamp_3)
            4 -> binding.stamp.setImageResource(R.drawable.ic_stamp_4)
            5 -> binding.stamp.setImageResource(R.drawable.ic_stamp_5)
            6 -> binding.stamp.setImageResource(R.drawable.ic_stamp_6)
            7 -> binding.stamp.setImageResource(R.drawable.ic_stamp_7)
            8 -> binding.stamp.setImageResource(R.drawable.ic_stamp_8)
            9 -> binding.stamp.setImageResource(R.drawable.ic_stamp_9)
            else -> binding.stamp.setImageResource(R.drawable.ic_stamp_1)
        }

        binding.stamp.setOnClickListener {
            interaction.onStampClick(stamp = model)
        }
    }

    override fun areItemsTheSame(other: BindableItem<*>): Boolean =
            other is StampBottomSheetStampListItem && other == this


    override fun areContentsTheSame(other: BindableItem<*>): Boolean =
            other is StampBottomSheetStampListItem && other == this
}

interface BottomSheetStampListClickInteraction {
    fun onStampClick(stamp: CompleteStampModel)
}