package com.polzzak_android.presentation.link.item

import com.polzzak_android.R
import com.polzzak_android.databinding.ItemLinkMainHeaderBinding
import com.polzzak_android.presentation.common.util.BindableItem

class LinkMainHeaderItem(private val text: String) : BindableItem<ItemLinkMainHeaderBinding>() {
    override val layoutRes: Int = R.layout.item_link_main_header

    override fun areItemsTheSame(other: BindableItem<*>): Boolean = other is LinkMainHeaderItem

    override fun areContentsTheSame(other: BindableItem<*>): Boolean =
        other is LinkMainHeaderItem && this.text == other.text

    override fun bind(binding: ItemLinkMainHeaderBinding, position: Int) {
        binding.tvRequestListTitle.text = text
    }

}