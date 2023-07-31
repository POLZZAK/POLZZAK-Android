package com.polzzak_android.presentation.feature.link.management.item

import com.polzzak_android.R
import com.polzzak_android.databinding.ItemLinkManagementMainEmptyBinding
import com.polzzak_android.presentation.common.util.BindableItem

class LinkManagementMainEmptyItem(private val content: String) :
    BindableItem<ItemLinkManagementMainEmptyBinding>() {
    override val layoutRes: Int = R.layout.item_link_management_main_empty

    override fun areItemsTheSame(other: BindableItem<*>): Boolean =
        other is LinkManagementMainEmptyItem

    override fun areContentsTheSame(other: BindableItem<*>): Boolean =
        other is LinkManagementMainEmptyItem && this.content == other.content

    override fun bind(binding: ItemLinkManagementMainEmptyBinding, position: Int) {
        binding.tvContent.text = content
    }
}
