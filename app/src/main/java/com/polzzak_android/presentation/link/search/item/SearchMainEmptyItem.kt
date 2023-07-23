package com.polzzak_android.presentation.link.search.item

import com.polzzak_android.R
import com.polzzak_android.databinding.ItemLinkMainEmptyBinding
import com.polzzak_android.presentation.common.util.BindableItem

class SearchMainEmptyItem(private val text: String) :
    BindableItem<ItemLinkMainEmptyBinding>() {
    override val layoutRes: Int = R.layout.item_link_main_empty
    override fun bind(binding: ItemLinkMainEmptyBinding, position: Int) {
        binding.tvContent.text = text
    }

    override fun areItemsTheSame(other: BindableItem<*>) = other is SearchMainEmptyItem

    override fun areContentsTheSame(other: BindableItem<*>) =
        other is SearchMainEmptyItem && this.text == other.text
}