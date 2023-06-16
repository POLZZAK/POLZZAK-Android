package com.polzzak_android.presentation.link.item

import com.polzzak_android.R
import com.polzzak_android.databinding.ItemLinkRequestEmptyBinding
import com.polzzak_android.presentation.common.util.BindableItem

class LinkRequestEmptyItem(private val content: String) :
    BindableItem<ItemLinkRequestEmptyBinding>() {
    override val layoutRes: Int = R.layout.item_link_request_empty

    override fun areItemsTheSame(other: BindableItem<*>): Boolean = other is LinkRequestEmptyItem

    override fun areContentsTheSame(other: BindableItem<*>): Boolean =
        other is LinkRequestEmptyItem && this.content == other.content

    override fun bind(binding: ItemLinkRequestEmptyBinding, position: Int) {
        binding.tvEmptyContent.text = content
    }
}