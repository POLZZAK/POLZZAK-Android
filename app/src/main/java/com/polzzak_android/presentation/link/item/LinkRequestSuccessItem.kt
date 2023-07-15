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
import com.polzzak_android.presentation.link.model.LinkRequestUserModel
import com.polzzak_android.presentation.link.LinkMainClickListener

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

    private class NormalItem(
        private val userModel: LinkRequestUserModel.Normal,
        private val clickListener: LinkMainClickListener
    ) : LinkRequestSuccessItem(userModel) {
        override fun areItemsTheSame(other: BindableItem<*>): Boolean =
            other is NormalItem && this.userModel.user.userId == other.userModel.user.userId

        override fun areContentsTheSame(other: BindableItem<*>): Boolean =
            other is NormalItem && this.userModel == other.userModel

        override fun bind(binding: ItemLinkRequestSuccessBinding, position: Int) {
            super.bind(binding, position)
            val context = binding.root.context
            with(binding) {
                tvBtnRequestCancel.isVisible = false
                tvNickName.isVisible = true
                tvNickName.text = userModel.user.nickName
                val btnText = context.getString(R.string.common_request_link)
                tvBtnRequest.text = btnText
                tvBtnRequest.setOnClickListener {
                    clickListener.displayRequestLinkDialog(linkUserModel = userModel.user)
                }
                tvBtnRequest.setTextColor(ContextCompat.getColor(context, R.color.white))
                tvBtnRequest.setBackgroundResource(R.drawable.shape_rectangle_primary_r4)
            }
        }
    }

    private class SentItem(
        private val userModel: LinkRequestUserModel.Sent,
        private val clickListener: LinkMainClickListener
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
                tvNickName.isVisible = true
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

    //TODO 온보딩 후 찾기 페이지에선 없음(연동관리 페이지 에서 디자인 및 추가구현 필요)
    private class LinkedItem(private val userModel: LinkRequestUserModel.Linked) :
        LinkRequestSuccessItem(userModel) {
        override fun areItemsTheSame(other: BindableItem<*>): Boolean =
            other is LinkedItem && this.userModel.user.userId == other.userModel.user.userId

        override fun areContentsTheSame(other: BindableItem<*>): Boolean =
            other is LinkedItem && this.userModel == other.userModel

        override fun bind(binding: ItemLinkRequestSuccessBinding, position: Int) {
            super.bind(binding, position)
            with(binding) {
                tvBtnRequestCancel.isVisible = false
                //TODO string resource, 버튼 background 적용
                tvBtnRequest.text = "이미 링크된 유저"
            }
        }
    }

    companion object {
        fun newInstance(
            userModel: LinkRequestUserModel.Normal,
            clickListener: LinkMainClickListener
        ): LinkRequestSuccessItem =
            NormalItem(userModel = userModel, clickListener = clickListener)

        fun newInstance(
            userModel: LinkRequestUserModel.Sent,
            clickListener: LinkMainClickListener
        ): LinkRequestSuccessItem =
            SentItem(userModel = userModel, clickListener = clickListener)

        fun newInstance(userModel: LinkRequestUserModel.Linked): LinkRequestSuccessItem =
            LinkedItem(userModel = userModel)
    }
}