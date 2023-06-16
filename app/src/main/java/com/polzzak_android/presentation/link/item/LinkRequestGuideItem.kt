package com.polzzak_android.presentation.link.item

import com.polzzak_android.R
import com.polzzak_android.databinding.ItemLinkRequestGuideBinding
import com.polzzak_android.presentation.common.util.BindableItem

class LinkRequestGuideItem(private val content: String) :
    BindableItem<ItemLinkRequestGuideBinding>() {
    override val layoutRes: Int = R.layout.item_link_request_guide

    override fun areItemsTheSame(other: BindableItem<*>): Boolean = other is LinkRequestGuideItem

    override fun areContentsTheSame(other: BindableItem<*>): Boolean =
        other is LinkRequestGuideItem && this.content == other.content

    override fun bind(binding: ItemLinkRequestGuideBinding, position: Int) {
        binding.tvGuideContent.text = content
    }
}