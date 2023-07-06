package com.polzzak_android.presentation.onboarding.item

import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import com.polzzak_android.R
import com.polzzak_android.common.util.convertDp2Px
import com.polzzak_android.databinding.ItemOnBoardingPageBinding
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.onboarding.OnBoardingClickListener
import com.polzzak_android.presentation.onboarding.model.OnBoardingPageModel

class OnBoardingPageItem(
    private val model: OnBoardingPageModel,
    private val clickListener: OnBoardingClickListener
) :
    BindableItem<ItemOnBoardingPageBinding>() {
    override val layoutRes: Int = R.layout.item_on_boarding_page

    override fun areItemsTheSame(other: BindableItem<*>): Boolean =
        other is OnBoardingPageItem && this.model.titleStringRes == other.model.titleStringRes

    override fun areContentsTheSame(other: BindableItem<*>): Boolean =
        other is OnBoardingPageItem && this.model == other.model

    override fun bind(binding: ItemOnBoardingPageBinding, position: Int) {
        with(binding) {
            tvTitle.text = binding.root.context.getString(model.titleStringRes)
            tvContent.text = binding.root.context.getString(model.contentStringRes)
            cpvProgress.maxCount = model.maxCount
            cpvProgress.checkedCount = model.progress
            val betweenMarginPx = convertDp2Px(binding.root.context, PROGRESS_BETWEEN_MARGIN_DP)
            cpvProgress.betweenMarginsPx = betweenMarginPx
            cpvProgress.doOnPreDraw {
                cpvProgress.drawableWidthPx =
                    (cpvProgress.width - (betweenMarginPx * model.maxCount - 1)) / model.maxCount
            }
            cpvProgress.isVisible = (model.progress < model.maxCount)
            tvBtnStart.visibility =
                if (model.progress == model.maxCount) View.VISIBLE else View.INVISIBLE
            cpvProgress.checkedCount = position + 1
            tvBtnStart.setOnClickListener {
                clickListener.onNext()
            }
        }
    }

    companion object {
        private const val PROGRESS_BETWEEN_MARGIN_DP = 5
    }
}