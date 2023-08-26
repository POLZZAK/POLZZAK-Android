package com.polzzak_android.presentation.component.bottomsheet.bindable

import android.view.View
import com.polzzak_android.R
import com.polzzak_android.databinding.ItemMissionBinding
import com.polzzak_android.databinding.ItemUserBinding
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.component.bottomsheet.model.SelectUserStampBoardModel
import com.polzzak_android.presentation.feature.stamp.model.MissionModel

class UserInfoListItem(
    val model: SelectUserStampBoardModel,
    private val interaction: BottomSheetUserInfoListClickInteraction
) :
    BindableItem<ItemUserBinding>() {
    override val layoutRes = R.layout.item_user

    var isSelected: Boolean = false

    override fun bind(binding: ItemUserBinding, position: Int) {
        with(binding) {
            userName = model.nickName
            userType = model.userType

            // 유저타입 뱃지 visibility
            if (model.userType == null) {
                binding.itemUserTypeBadge.visibility = View.GONE
            } else {
                binding.itemUserTypeBadge.visibility = View.VISIBLE
            }

            // isSelected에 따른 view와 value 세팅
            root.isSelected = isSelected
            if (isSelected) {
                itemUserNickname.setTextColor(root.context.getColor(R.color.primary))
                itemUserCheck.visibility = View.VISIBLE
            } else {
                itemUserNickname.setTextColor(root.context.getColor(R.color.black))
                itemUserCheck.visibility = View.GONE
            }

            root.setOnClickListener {
                isSelected = !isSelected
                it.isSelected = isSelected

                if (isSelected) {
                    binding.itemUserNickname.setTextColor(it.context.getColor(R.color.primary))
                    itemUserCheck.visibility = View.VISIBLE
                } else {
                    binding.itemUserNickname.setTextColor(it.context.getColor(R.color.black))
                    itemUserCheck.visibility = View.GONE
                }

                interaction.onUserClick(model)
            }
        }
    }

    override fun areItemsTheSame(other: BindableItem<*>): Boolean =
        other is UserInfoListItem && other.model.userId == this.model.userId


    override fun areContentsTheSame(other: BindableItem<*>): Boolean =
        other is UserInfoListItem && other.model == this.model
}

interface BottomSheetUserInfoListClickInteraction {
    fun onUserClick(model: SelectUserStampBoardModel)
}