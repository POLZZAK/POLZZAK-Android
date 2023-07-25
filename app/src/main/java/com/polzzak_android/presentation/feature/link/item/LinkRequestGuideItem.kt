package com.polzzak_android.presentation.feature.link.item

import com.polzzak_android.R
import com.polzzak_android.databinding.ItemLinkRequestGuideBinding
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.feature.link.model.LinkRequestUserModel

class LinkRequestGuideItem(private val model: LinkRequestUserModel.Guide) :
    BindableItem<ItemLinkRequestGuideBinding>() {
    override val layoutRes: Int = R.layout.item_link_request_guide

    override fun areItemsTheSame(other: BindableItem<*>): Boolean = other is LinkRequestGuideItem

    override fun areContentsTheSame(other: BindableItem<*>): Boolean =
        other is LinkRequestGuideItem && this.model == other.model

    override fun bind(binding: ItemLinkRequestGuideBinding, position: Int) {
        val context = binding.root.context
        val targetText = context.getString(model.targetLinkMemberType.stringRes)
        val contentText = context.getString(R.string.search_request_guide_text, targetText)
        binding.root.context
        binding.tvContent.text = contentText
    }
}