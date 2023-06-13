package com.polzzak_android.presentation.search.item

import com.bumptech.glide.Glide
import com.polzzak_android.R
import com.polzzak_android.databinding.ItemSearchMainRequestBinding
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.search.base.BaseSearchClickListener
import com.polzzak_android.presentation.search.model.SearchMainRequestModel

class SearchMainRequestItem(
    private val model: SearchMainRequestModel,
    private val clickListener: BaseSearchClickListener
) :
    BindableItem<ItemSearchMainRequestBinding>() {
    override val layoutRes = R.layout.item_search_main_request
    override fun bind(binding: ItemSearchMainRequestBinding, position: Int) {
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
        other is SearchMainRequestItem && other.model.userId == this.model.userId

    override fun areContentsTheSame(other: BindableItem<*>): Boolean =
        other is SearchMainRequestItem && other.model == this.model
}