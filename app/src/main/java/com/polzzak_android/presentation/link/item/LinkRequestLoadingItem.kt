package com.polzzak_android.presentation.link.item

import com.polzzak_android.R
import com.polzzak_android.databinding.ItemLinkRequestLoadingBinding
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.link.search.SearchClickListener

class LinkRequestLoadingItem(
    private val nickName: String,
    private val clickListener: SearchClickListener
) : BindableItem<ItemLinkRequestLoadingBinding>() {
    override val layoutRes: Int = R.layout.item_link_request_loading

    override fun areItemsTheSame(other: BindableItem<*>): Boolean = other is LinkRequestLoadingItem

    override fun areContentsTheSame(other: BindableItem<*>): Boolean =
        other is LinkRequestLoadingItem && this.nickName == other.nickName

    override fun bind(binding: ItemLinkRequestLoadingBinding, position: Int) {
        binding.tvContent.text =
            binding.root.context.getString(R.string.search_request_loading_text, nickName)
        binding.tvBtnCancel.setOnClickListener {
            clickListener.cancelSearch()
        }
    }
}