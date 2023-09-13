package com.polzzak_android.presentation.common.item

import com.polzzak_android.R
import com.polzzak_android.databinding.ItemErrorRefreshBinding
import com.polzzak_android.presentation.common.util.BindableItem

class ErrorRefreshItem(val content: String, val onRefreshClick: () -> Unit) :
    BindableItem<ItemErrorRefreshBinding>() {
    override val layoutRes: Int = R.layout.item_error_refresh
    override fun areItemsTheSame(other: BindableItem<*>): Boolean =
        other is ErrorRefreshItem && this.content == other.content

    override fun areContentsTheSame(other: BindableItem<*>): Boolean =
        other is ErrorRefreshItem && this.content == other.content && this.onRefreshClick == other.onRefreshClick

    override fun bind(binding: ItemErrorRefreshBinding, position: Int) {
        with(binding) {
            tvContent.text = content
            ivBtnRefresh.setOnClickListener { onRefreshClick.invoke() }
        }
    }
}