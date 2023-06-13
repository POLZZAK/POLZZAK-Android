package com.polzzak_android.presentation.search.item

import com.polzzak_android.R
import com.polzzak_android.databinding.ItemSearchMainRequestEmptyBinding
import com.polzzak_android.presentation.common.util.BindableItem

class SearchMainRequestEmptyItem :
    BindableItem<ItemSearchMainRequestEmptyBinding>() {
    override val layoutRes: Int = R.layout.item_search_main_request_empty
    override fun bind(binding: ItemSearchMainRequestEmptyBinding, position: Int) {
        //do nothing
    }

    override fun areItemsTheSame(other: BindableItem<*>) = other is SearchMainRequestEmptyItem

    override fun areContentsTheSame(other: BindableItem<*>) =
        other is SearchMainRequestEmptyItem
}