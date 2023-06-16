package com.polzzak_android.presentation.link.item

import androidx.core.view.isVisible
import com.polzzak_android.R
import com.polzzak_android.common.util.loadCircleImageUrl
import com.polzzak_android.databinding.ItemLinkRequestSuccessBinding
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.link.model.LinkUserModel
import com.polzzak_android.presentation.link.model.LinkUserStatusModel

class LinkRequestSuccessItem(
    private val userModel: LinkUserModel,
    private val statusModel: LinkUserStatusModel
) :
    BindableItem<ItemLinkRequestSuccessBinding>() {
    override val layoutRes: Int = R.layout.item_link_request_success

    //TODO model 클래스 구현
    override fun areItemsTheSame(other: BindableItem<*>) =
        other is LinkRequestSuccessItem && this.userModel.userId == other.userModel.userId

    override fun areContentsTheSame(other: BindableItem<*>) =
        other is LinkRequestSuccessItem && this.userModel == other.userModel && this.statusModel == other.statusModel

    override fun bind(binding: ItemLinkRequestSuccessBinding, position: Int) {
        with(binding) {
            userModel.profileUrl?.let { binding.ivSuccessProfile.loadCircleImageUrl(imageUrl = it) }
            tvSuccessBtnRequestCancel.isVisible = (statusModel == LinkUserStatusModel.SENT)
            tvSuccessBtnRequestCancel.setOnClickListener{
                //TODO 연동 취소 요청
            }
            when (statusModel) {
                LinkUserStatusModel.NORMAL -> {
                    //TODO 연동요청
                }

                LinkUserStatusModel.SENT -> {
                    //TODO 이미 연동했어요
                }

                LinkUserStatusModel.LINKED -> {
                    //TODO 연동 요청 완료
                }

                else -> {
                    //do nothing
                }
            }
        }
    }
}