package com.polzzak_android.presentation.onboarding.item

import androidx.core.view.isVisible
import com.polzzak_android.R
import com.polzzak_android.databinding.ItemOnBoardingPageBinding
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.onboarding.model.OnBoardingPageModel

class OnBoardingPageItem(private val model: OnBoardingPageModel) :
    BindableItem<ItemOnBoardingPageBinding>() {
    override val layoutRes: Int = R.layout.item_on_boarding_page

    override fun areItemsTheSame(other: BindableItem<*>): Boolean =
        other is OnBoardingPageItem && this.model.title == other.model.title

    override fun areContentsTheSame(other: BindableItem<*>): Boolean =
        other is OnBoardingPageItem && this.model == other.model

    override fun bind(binding: ItemOnBoardingPageBinding, position: Int) {
        with(binding) {
            tvTitle.text = model.title
            tvContent.text = model.content
            tvBtnStart.isVisible = (model.progress == model.maxCount)
            cpvProgress.isVisible = (position < model.maxCount)
            cpvProgress.checkedCount = (model.progress)
            cpvProgress.maxCount = (model.maxCount)
        }
    }
}