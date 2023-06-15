package com.polzzak_android.presentation.link.item

import com.bumptech.glide.Glide
import com.polzzak_android.R
import com.polzzak_android.databinding.ItemLinkMainSentRequestBinding
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.link.search.base.BaseSearchClickListener
import com.polzzak_android.presentation.link.model.LinkUserModel

class LinkMainSentRequestItem(
    private val model: LinkUserModel,
    private val clickListener: BaseSearchClickListener
) :
    BindableItem<ItemLinkMainSentRequestBinding>() {
    override val layoutRes = R.layout.item_link_main_sent_request
    override fun bind(binding: ItemLinkMainSentRequestBinding, position: Int) {
        with(binding) {
            tvNickName.text = model.nickName
            Glide.with(root.context).load(model.profileUrl)
                .into(ivProfileImage)
            tvBtnRequestCancel.setOnClickListener {
                clickListener.displayCancelRequestDialog()
            }
        }
    }

    override fun areItemsTheSame(other: BindableItem<*>): Boolean =
        other is LinkMainSentRequestItem && other.model.userId == this.model.userId

    override fun areContentsTheSame(other: BindableItem<*>): Boolean =
        other is LinkMainSentRequestItem && other.model == this.model
}