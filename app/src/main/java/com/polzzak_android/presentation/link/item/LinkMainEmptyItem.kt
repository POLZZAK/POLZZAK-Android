package com.polzzak_android.presentation.link.item

import com.polzzak_android.R
import com.polzzak_android.databinding.ItemLinkMainEmptyBinding
import com.polzzak_android.presentation.common.util.BindableItem

class LinkMainEmptyItem(private val content: String) :
    BindableItem<ItemLinkMainEmptyBinding>() {
    override val layoutRes: Int = R.layout.item_link_main_empty
    override fun bind(binding: ItemLinkMainEmptyBinding, position: Int) {
        binding.tvContent.text = content
    }

    override fun areItemsTheSame(other: BindableItem<*>) = other is LinkMainEmptyItem

    override fun areContentsTheSame(other: BindableItem<*>) =
        other is LinkMainEmptyItem && this.content == other.content
}