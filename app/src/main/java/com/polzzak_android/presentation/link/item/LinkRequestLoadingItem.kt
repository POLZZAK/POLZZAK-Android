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
        //TODO string resource 적용
        binding.tvContent.text = "${nickName}님을\n열심히 찾는 중이에요"
        binding.tvBtnCancel.setOnClickListener {
            clickListener.cancelSearch()
        }
    }
}