package com.polzzak_android.presentation.link.item

import com.polzzak_android.R
import com.polzzak_android.common.util.loadCircleImageUrl
import com.polzzak_android.databinding.ItemLinkLinkedUserBinding
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.link.model.LinkUserModel

class LinkMainLinkedUserItem(private val model: LinkUserModel) :
    BindableItem<ItemLinkLinkedUserBinding>() {
    override val layoutRes: Int = R.layout.item_link_linked_user
    override fun areItemsTheSame(other: BindableItem<*>): Boolean =
        other is LinkMainLinkedUserItem && this.model.userId == other.model.userId

    override fun areContentsTheSame(other: BindableItem<*>): Boolean =
        other is LinkMainLinkedUserItem && this.model == other.model

    override fun bind(binding: ItemLinkLinkedUserBinding, position: Int) {
        with(binding) {
            ivProfileImage.loadCircleImageUrl(imageUrl = model.profileUrl)
            tvNickName.text = model.nickName
            ivBtnCancelLink.setOnClickListener {
                //TODO 연동 취소?
            }
        }
    }

}