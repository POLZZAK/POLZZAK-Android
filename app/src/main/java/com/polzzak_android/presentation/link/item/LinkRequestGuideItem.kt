package com.polzzak_android.presentation.link.item

import com.polzzak_android.R
import com.polzzak_android.databinding.ItemLinkRequestGuideBinding
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.link.model.LinkRequestUserModel

class LinkRequestGuideItem(private val model: LinkRequestUserModel.Guide) :
    BindableItem<ItemLinkRequestGuideBinding>() {
    override val layoutRes: Int = R.layout.item_link_request_guide

    override fun areItemsTheSame(other: BindableItem<*>): Boolean = other is LinkRequestGuideItem

    override fun areContentsTheSame(other: BindableItem<*>): Boolean =
        other is LinkRequestGuideItem && this.model == other.model

    override fun bind(binding: ItemLinkRequestGuideBinding, position: Int) {
        val targetString = binding.root.context.getString(model.targetLinkMemberType.stringRes)
        //TODO string resource 적용
        binding.tvContent.text = "연동된 ${targetString}에게\n칭찬 도장판을 만들어 줄 수 있어요"
    }
}