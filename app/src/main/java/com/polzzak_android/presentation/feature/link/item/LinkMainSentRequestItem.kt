package com.polzzak_android.presentation.feature.link.item

import com.polzzak_android.R
import com.polzzak_android.presentation.common.util.loadCircleImageUrl
import com.polzzak_android.databinding.ItemLinkMainSentRequestBinding
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.feature.link.model.LinkUserModel
import com.polzzak_android.presentation.feature.link.LinkClickListener

class LinkMainSentRequestItem(
    private val model: LinkUserModel,
    private val clickListener: LinkClickListener
) : BindableItem<ItemLinkMainSentRequestBinding>() {
    override val layoutRes = R.layout.item_link_main_sent_request
    override fun bind(binding: ItemLinkMainSentRequestBinding, position: Int) {
        with(binding) {
            tvNickName.text = model.nickName
            ivProfileImage.loadCircleImageUrl(imageUrl = model.profileUrl)
            tvBtnRequestCancel.setOnClickListener {
                clickListener.displayCancelRequestDialog(linkUserModel = model)
            }
        }
    }

    override fun areItemsTheSame(other: BindableItem<*>): Boolean =
        other is LinkMainSentRequestItem && other.model.userId == this.model.userId

    override fun areContentsTheSame(other: BindableItem<*>): Boolean =
        other is LinkMainSentRequestItem && other.model == this.model
}