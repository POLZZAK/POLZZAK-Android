package com.polzzak_android.presentation.link.item

import androidx.annotation.CallSuper
import androidx.core.view.isVisible
import com.polzzak_android.R
import com.polzzak_android.common.util.loadCircleImageUrl
import com.polzzak_android.databinding.ItemLinkRequestSuccessBinding
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.link.model.LinkRequestUserModel

abstract class LinkRequestSuccessItem(
    private val userModel: LinkRequestUserModel,
) : BindableItem<ItemLinkRequestSuccessBinding>() {
    override val layoutRes: Int = R.layout.item_link_request_success

    @CallSuper
    override fun bind(binding: ItemLinkRequestSuccessBinding, position: Int) {
        with(binding) {
            userModel.user?.let { user ->
                ivProfile.loadCircleImageUrl(imageUrl = user.profileUrl)
            }
        }
    }

    private class NormalItem(private val userModel: LinkRequestUserModel.Normal) :
        LinkRequestSuccessItem(userModel) {
        override fun areItemsTheSame(other: BindableItem<*>): Boolean =
            other is NormalItem && this.userModel.user.userId == other.userModel.user.userId

        override fun areContentsTheSame(other: BindableItem<*>): Boolean =
            other is NormalItem && this.userModel == other.userModel

        override fun bind(binding: ItemLinkRequestSuccessBinding, position: Int) {
            super.bind(binding, position)
            with(binding) {
                tvBtnRequestCancel.isVisible = false
                //TODO string resource, 버튼 background 적용
                tvBtnRequest.text = "연동신청"
            }
        }
    }

    private class SentItem(private val userModel: LinkRequestUserModel.Sent) :
        LinkRequestSuccessItem(userModel) {
        override fun areItemsTheSame(other: BindableItem<*>): Boolean =
            other is SentItem && this.userModel.user.userId == other.userModel.user.userId

        override fun areContentsTheSame(other: BindableItem<*>): Boolean =
            other is SentItem && this.userModel == other.userModel

        override fun bind(binding: ItemLinkRequestSuccessBinding, position: Int) {
            super.bind(binding, position)
            with(binding) {
                tvBtnRequestCancel.isVisible = false
                //TODO string resource, 버튼 background 적용
                tvBtnRequest.text = "이미 보낸 요청"
            }
        }
    }

    private class LinkedItem(private val userModel: LinkRequestUserModel.Linked) :
        LinkRequestSuccessItem(userModel) {
        override fun areItemsTheSame(other: BindableItem<*>): Boolean =
            other is LinkedItem && this.userModel.user.userId == other.userModel.user.userId

        override fun areContentsTheSame(other: BindableItem<*>): Boolean =
            other is LinkedItem && this.userModel == other.userModel

        override fun bind(binding: ItemLinkRequestSuccessBinding, position: Int) {
            super.bind(binding, position)
            with(binding) {
                tvBtnRequestCancel.isVisible = true
                //TODO string resource, 버튼 background 적용
                tvBtnRequest.text = "이미 링크된 유저"
            }
        }
    }

    companion object {
        fun newInstance(userModel: LinkRequestUserModel.Normal): LinkRequestSuccessItem =
            NormalItem(userModel = userModel)

        fun newInstance(userModel: LinkRequestUserModel.Sent): LinkRequestSuccessItem =
            SentItem(userModel = userModel)

        fun newInstance(userModel: LinkRequestUserModel.Linked): LinkRequestSuccessItem =
            LinkedItem(userModel = userModel)
    }
}