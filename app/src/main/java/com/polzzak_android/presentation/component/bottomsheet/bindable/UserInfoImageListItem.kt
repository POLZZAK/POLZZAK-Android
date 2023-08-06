package com.polzzak_android.presentation.component.bottomsheet.bindable

import android.view.View
import com.polzzak_android.R
import com.polzzak_android.databinding.ItemProfileBinding
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.component.bottomsheet.model.SelectUserMakeBoardModelModel

class UserInfoImageListItem(
    val model: SelectUserMakeBoardModelModel,
    private val interaction: BottomSheetUserInfoImageListClickInteraction
) :
    BindableItem<ItemProfileBinding>() {
    override val layoutRes = R.layout.item_profile

    var isSelected: Boolean = false

    override fun bind(binding: ItemProfileBinding, position: Int) {
        with(binding) {
            userName = model.nickName

            // isSelected에 따른 view와 value 세팅
            root.isSelected = isSelected
            if (isSelected) {
                itemProfileNickname.setTextColor(root.context.getColor(R.color.primary))
                itemProfileCheck.visibility = View.VISIBLE
            } else {
                itemProfileNickname.setTextColor(root.context.getColor(R.color.black))
                itemProfileCheck.visibility = View.GONE
            }

            root.setOnClickListener {
                isSelected = !isSelected
                it.isSelected = isSelected

                if (isSelected) {
                    binding.itemProfileNickname.setTextColor(it.context.getColor(R.color.primary))
                    itemProfileCheck.visibility = View.VISIBLE
                } else {
                    binding.itemProfileNickname.setTextColor(it.context.getColor(R.color.black))
                    itemProfileCheck.visibility = View.GONE
                }

                interaction.onUserProfileClick(model)
            }
        }
    }

    override fun areItemsTheSame(other: BindableItem<*>): Boolean =
        other is UserInfoImageListItem && other.model.userId == this.model.userId


    override fun areContentsTheSame(other: BindableItem<*>): Boolean =
        other is UserInfoImageListItem && other.model == this.model
}

interface BottomSheetUserInfoImageListClickInteraction {
    fun onUserProfileClick(model: SelectUserMakeBoardModelModel)
}