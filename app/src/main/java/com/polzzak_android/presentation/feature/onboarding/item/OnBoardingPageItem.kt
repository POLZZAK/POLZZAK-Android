package com.polzzak_android.presentation.feature.onboarding.item

import com.polzzak_android.R
import com.polzzak_android.databinding.ItemOnBoardingPageBinding
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.feature.onboarding.model.OnBoardingPageModel

class OnBoardingPageItem(
    private val model: OnBoardingPageModel,
) : BindableItem<ItemOnBoardingPageBinding>() {
    override val layoutRes: Int = R.layout.item_on_boarding_page

    override fun areItemsTheSame(other: BindableItem<*>): Boolean =
        other is OnBoardingPageItem && this.model.titleStringRes == other.model.titleStringRes

    override fun areContentsTheSame(other: BindableItem<*>): Boolean =
        other is OnBoardingPageItem && this.model == other.model

    override fun bind(binding: ItemOnBoardingPageBinding, position: Int) {
        with(binding) {
            tvTitle.text = binding.root.context.getString(model.titleStringRes)
            tvContent.text = binding.root.context.getString(model.contentStringRes)
            ivIcon.setImageResource(model.imageDrawableRes)
        }
    }
}