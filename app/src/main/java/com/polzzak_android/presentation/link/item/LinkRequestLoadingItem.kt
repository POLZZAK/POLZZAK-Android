package com.polzzak_android.presentation.link.item

import com.polzzak_android.R
import com.polzzak_android.databinding.ItemLinkRequestLoadingBinding
import com.polzzak_android.presentation.common.util.BindableItem

class LinkRequestLoadingItem : BindableItem<ItemLinkRequestLoadingBinding>() {
    override val layoutRes: Int = R.layout.item_link_request_loading

    override fun areItemsTheSame(other: BindableItem<*>): Boolean = other is LinkRequestLoadingItem

    override fun areContentsTheSame(other: BindableItem<*>): Boolean =
        other is LinkRequestLoadingItem

    override fun bind(binding: ItemLinkRequestLoadingBinding, position: Int) {
        //do nothing
    }
}