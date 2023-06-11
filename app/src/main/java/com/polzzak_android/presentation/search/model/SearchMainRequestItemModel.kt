package com.polzzak_android.presentation.search.model

import com.bumptech.glide.Glide
import com.polzzak_android.R
import com.polzzak_android.databinding.ItemSearchMainRequestBinding
import com.polzzak_android.presentation.common.util.BindableItem

class SearchMainRequestItemModel(private val model: SearchMainRequestModel) :
    BindableItem<ItemSearchMainRequestBinding>() {
    override val layoutRes = R.layout.item_search_main_request
    override fun bind(position: Int) {
        binding?.let { binding ->
            with(binding) {
                tvNickName.text = model.nickName
                Glide.with(root.context).load(model.profileUrl)
                    .into(ivProfileImage)
            }
        }
    }

    override fun areItemsTheSame(other: BindableItem<*>): Boolean =
        other is SearchMainRequestItemModel && other.model.userId == this.model.userId

    override fun areContentsTheSame(other: BindableItem<*>): Boolean =
        other is SearchMainRequestItemModel && other.model == this.model
}