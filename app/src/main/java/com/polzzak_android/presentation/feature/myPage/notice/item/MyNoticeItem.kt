package com.polzzak_android.presentation.feature.myPage.notice.item

import com.polzzak_android.R
import com.polzzak_android.databinding.ItemMyNoticeBinding
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.common.util.toPublishedDateString
import com.polzzak_android.presentation.feature.myPage.notice.MyNoticeClickListener
import com.polzzak_android.presentation.feature.myPage.notice.model.MyNoticeModel

class MyNoticeItem(
    private val model: MyNoticeModel,
    private val clickListener: MyNoticeClickListener
) : BindableItem<ItemMyNoticeBinding>() {
    override val layoutRes: Int = R.layout.item_my_notice
    override fun areItemsTheSame(other: BindableItem<*>): Boolean =
        other is MyNoticeItem && this.model.id == other.model.id

    override fun areContentsTheSame(other: BindableItem<*>): Boolean =
        other is MyNoticeItem && this.model == other.model

    override fun bind(binding: ItemMyNoticeBinding, position: Int) {
        with(binding) {
            root.setOnClickListener {
                clickListener.onClickNotice(model = model)
            }
            tvTitle.text = model.title
            tvDate.text = model.date.toLocalDate().toPublishedDateString()
            tvContent.text = model.content.replace(Regex("(\\r\\n|\\r|\\n)+"), " ")
        }
    }
}