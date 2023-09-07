package com.polzzak_android.presentation.common.item

import com.polzzak_android.R
import com.polzzak_android.databinding.ItemFullLoadingBinding
import com.polzzak_android.presentation.common.util.BindableItem

class FullLoadingItem : BindableItem<ItemFullLoadingBinding>() {
    override val layoutRes: Int = R.layout.item_full_loading

    override fun areItemsTheSame(other: BindableItem<*>): Boolean = other is FullLoadingItem

    override fun areContentsTheSame(other: BindableItem<*>): Boolean = other is FullLoadingItem

    override fun bind(binding: ItemFullLoadingBinding, position: Int) {
        //do nothing
    }
}