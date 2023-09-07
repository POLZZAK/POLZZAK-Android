package com.polzzak_android.presentation.feature.myPage.notice.item

import com.polzzak_android.R
import com.polzzak_android.databinding.ItemMyNoticieEmptyBinding
import com.polzzak_android.presentation.common.util.BindableItem

class MyNoticeEmptyItem : BindableItem<ItemMyNoticieEmptyBinding>() {
    override val layoutRes: Int = R.layout.item_my_noticie_empty
    override fun areItemsTheSame(other: BindableItem<*>): Boolean = other is MyNoticeEmptyItem

    override fun areContentsTheSame(other: BindableItem<*>): Boolean = other is MyNoticeEmptyItem

    override fun bind(binding: ItemMyNoticieEmptyBinding, position: Int) {
        //do nothing
    }

}