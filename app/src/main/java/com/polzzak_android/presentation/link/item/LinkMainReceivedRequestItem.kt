package com.polzzak_android.presentation.link.item

import com.polzzak_android.R
import com.polzzak_android.common.util.loadCircleImageUrl
import com.polzzak_android.databinding.ItemLinkMainReceivedRequestBinding
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.link.model.LinkUserModel

class LinkMainReceivedRequestItem(private val model: LinkUserModel) :
    BindableItem<ItemLinkMainReceivedRequestBinding>() {
    override val layoutRes: Int = R.layout.item_link_main_received_request

    override fun areItemsTheSame(other: BindableItem<*>): Boolean =
        other is LinkMainReceivedRequestItem && this.model.userId == other.model.userId

    override fun areContentsTheSame(other: BindableItem<*>): Boolean =
        other is LinkMainReceivedRequestItem && this.model == other.model


    override fun bind(binding: ItemLinkMainReceivedRequestBinding, position: Int) {
        with(binding) {
            ivProfileImage.loadCircleImageUrl(imageUrl = model.profileUrl)
            tvNickName.text = model.nickName
            tvBtnAccept.setOnClickListener {
                //TODO 연동 요청 수락
            }
            tvBtnDecline.setOnClickListener {
                //TODO 연동 요청 거절
            }
        }
    }
}