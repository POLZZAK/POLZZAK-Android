package com.polzzak_android.presentation.link.item

import android.text.SpannableString
import android.text.style.UnderlineSpan
import androidx.annotation.CallSuper
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.polzzak_android.R
import com.polzzak_android.common.util.loadCircleImageUrl
import com.polzzak_android.databinding.ItemLinkRequestSuccessBinding
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.component.PolzzakSnackBar
import com.polzzak_android.presentation.link.model.LinkRequestUserModel
import com.polzzak_android.presentation.link.LinkClickListener

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

    private abstract class BaseNormalItem(
        private val userModel: LinkRequestUserModel,
    ) : LinkRequestSuccessItem(userModel) {
        override fun areItemsTheSame(other: BindableItem<*>): Boolean =
            other is BaseNormalItem && this.userModel.user?.userId == other.userModel.user?.userId

        override fun areContentsTheSame(other: BindableItem<*>): Boolean =
            other is BaseNormalItem && this.userModel == other.userModel

        override fun bind(binding: ItemLinkRequestSuccessBinding, position: Int) {
            super.bind(binding, position)
            val context = binding.root.context
            with(binding) {
                tvBtnRequestCancel.isVisible = false
                tvNickName.text = userModel.user?.nickName ?: ""
                val btnText = context.getString(R.string.common_request_link)
                tvBtnRequest.text = btnText
                tvBtnRequest.setOnClickListener {
                    onBtnRequestClick(binding = binding)
                }
                tvBtnRequest.setTextColor(ContextCompat.getColor(context, R.color.white))
                tvBtnRequest.setBackgroundResource(R.drawable.shape_rectangle_primary_r4)
            }
        }

        abstract fun onBtnRequestClick(binding: ItemLinkRequestSuccessBinding)

        class NormalItem(
            private val userModel: LinkRequestUserModel.Normal,
            private val clickListener: LinkClickListener
        ) : BaseNormalItem(userModel = userModel) {
            override fun onBtnRequestClick(binding: ItemLinkRequestSuccessBinding) {
                clickListener.displayRejectRequestDialog(linkUserModel = userModel.user)
            }
        }

        class ReceivedItem(userModel: LinkRequestUserModel.Received) :
            BaseNormalItem(userModel = userModel) {
            override fun onBtnRequestClick(binding: ItemLinkRequestSuccessBinding) {
                PolzzakSnackBar.make(
                    binding.root,
                    R.string.link_management_request_to_received,
                    PolzzakSnackBar.Type.WARNING
                ).show()
            }

        }
    }

    private class SentItem(
        private val userModel: LinkRequestUserModel.Sent,
        private val clickListener: LinkClickListener
    ) : LinkRequestSuccessItem(userModel) {
        override fun areItemsTheSame(other: BindableItem<*>): Boolean =
            other is SentItem && this.userModel.user.userId == other.userModel.user.userId

        override fun areContentsTheSame(other: BindableItem<*>): Boolean =
            other is SentItem && this.userModel == other.userModel

        override fun bind(binding: ItemLinkRequestSuccessBinding, position: Int) {
            super.bind(binding, position)
            with(binding) {
                val context = root.context
                tvBtnRequestCancel.isVisible = true
                tvNickName.text = userModel.user.nickName
                tvBtnRequest.text = context.getString(R.string.search_request_request_complete)
                tvBtnRequest.setTextColor(ContextCompat.getColor(context, R.color.primary))
                tvBtnRequest.setBackgroundResource(R.drawable.shape_rectangle_white_stroke_primary_r4)
                val btnRequestCancelText =
                    context.getString(R.string.search_request_btn_cancel_request)
                val btnRequestCancelSpannable = SpannableString(btnRequestCancelText).apply {
                    setSpan(UnderlineSpan(), 0, btnRequestCancelText.length, 0)
                }
                tvBtnRequestCancel.text = btnRequestCancelSpannable
                tvBtnRequestCancel.setOnClickListener {
                    clickListener.displayCancelRequestDialog(linkUserModel = userModel.user)
                }
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
                val context = root.context
                tvBtnRequestCancel.isVisible = false
                tvNickName.text = userModel.user.nickName
                val text = context.getString(R.string.link_management_search_linked)
                tvBtnRequest.text = text
                tvBtnRequest.setTextColor(ContextCompat.getColor(context, R.color.gray_400))
                tvBtnRequest.setBackgroundResource(R.drawable.shape_rectangle_white_stroke_gray_400_r4)
            }
        }
    }

    companion object {
        fun newInstance(
            userModel: LinkRequestUserModel.Normal,
            clickListener: LinkClickListener
        ): LinkRequestSuccessItem =
            BaseNormalItem.NormalItem(userModel = userModel, clickListener = clickListener)

        fun newInstance(
            userModel: LinkRequestUserModel.Received
        ): LinkRequestSuccessItem = BaseNormalItem.ReceivedItem(userModel = userModel)

        fun newInstance(
            userModel: LinkRequestUserModel.Sent,
            clickListener: LinkClickListener
        ): LinkRequestSuccessItem =
            SentItem(userModel = userModel, clickListener = clickListener)

        fun newInstance(userModel: LinkRequestUserModel.Linked): LinkRequestSuccessItem =
            LinkedItem(userModel = userModel)
    }
}