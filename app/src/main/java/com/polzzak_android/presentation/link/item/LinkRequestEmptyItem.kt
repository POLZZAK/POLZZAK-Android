package com.polzzak_android.presentation.link.item

import com.polzzak_android.R
import com.polzzak_android.databinding.ItemLinkRequestEmptyBinding
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.link.model.LinkRequestUserModel

class LinkRequestEmptyItem(private val model: LinkRequestUserModel.Empty) :
    BindableItem<ItemLinkRequestEmptyBinding>() {
    override val layoutRes: Int = R.layout.item_link_request_empty

    override fun areItemsTheSame(other: BindableItem<*>): Boolean = other is LinkRequestEmptyItem

    override fun areContentsTheSame(other: BindableItem<*>): Boolean =
        other is LinkRequestEmptyItem && this.model == other.model

    override fun bind(binding: ItemLinkRequestEmptyBinding, position: Int) {
        //TODO string resource 적용
        binding.tvContent.text = "${model.nickName}님을\n찾을 수 없어요"
    }
}