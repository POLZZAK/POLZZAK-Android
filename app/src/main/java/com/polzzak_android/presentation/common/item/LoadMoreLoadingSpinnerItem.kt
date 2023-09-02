package com.polzzak_android.presentation.common.item

import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.constraintlayout.widget.ConstraintLayout
import com.polzzak_android.R
import com.polzzak_android.databinding.ItemLoadMoreLoadingBinding
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.common.util.toPx

class LoadMoreLoadingSpinnerItem(
    private val marginTopDp: Int = DEFAULT_VERTICAL_MARGIN_DP,
    private val marginBottomDp: Int = DEFAULT_VERTICAL_MARGIN_DP
) :
    BindableItem<ItemLoadMoreLoadingBinding>() {
    override val layoutRes: Int = R.layout.item_load_more_loading

    override fun areItemsTheSame(other: BindableItem<*>): Boolean =
        other is LoadMoreLoadingSpinnerItem

    override fun areContentsTheSame(other: BindableItem<*>): Boolean =
        other is LoadMoreLoadingSpinnerItem && this.marginTopDp == other.marginTopDp &&
                this.marginBottomDp == other.marginBottomDp

    override fun bind(binding: ItemLoadMoreLoadingBinding, position: Int) {
        val context = binding.root.context
        (binding.ivSpinner.layoutParams as? ConstraintLayout.LayoutParams)?.let { lp ->
            binding.ivSpinner.layoutParams = lp.apply {
                topMargin = marginTopDp.toPx(context)
                bottomMargin = marginBottomDp.toPx(context)
            }
        }
        val rotateAnimation = RotateAnimation(
            0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 1000
            interpolator = LinearInterpolator()
            repeatCount = Animation.INFINITE
        }
        binding.ivSpinner.startAnimation(rotateAnimation)

    }

    companion object {
        private const val DEFAULT_VERTICAL_MARGIN_DP = 24
    }
}