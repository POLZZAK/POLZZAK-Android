package com.polzzak_android.presentation.feature.onboarding.protector

import com.polzzak_android.R
import com.polzzak_android.presentation.feature.onboarding.base.BaseOnBoardingFragment
import com.polzzak_android.presentation.feature.onboarding.model.OnBoardingPageModel

class ProtectorOnBoardingFragment : BaseOnBoardingFragment() {
    private val titleStringResList = listOf(
        R.string.on_boarding_parent_first_title,
        R.string.on_boarding_parent_second_title,
        R.string.on_boarding_parent_third_title,
        R.string.on_boarding_parent_fourth_title,
        R.string.on_boarding_parent_fifth_title
    )
    private val contentStringResList = listOf(
        R.string.on_boarding_parent_first_content,
        R.string.on_boarding_parent_second_content,
        R.string.on_boarding_parent_third_content,
        R.string.on_boarding_parent_fourth_content,
        R.string.on_boarding_parent_fifth_content
    )
    private val imageDrawableResList = listOf(
        R.drawable.ic_onboarding_first,
        R.drawable.ic_onboarding_second,
        R.drawable.ic_onboarding_third,
        R.drawable.ic_onboarding_parent_fourth,
        R.drawable.ic_onboarding_parent_fifth
    )

    override val pageData = List(5) {
        OnBoardingPageModel(
            titleStringRes = titleStringResList[it],
            contentStringRes = contentStringResList[it],
            imageDrawableRes = imageDrawableResList[it],
            progress = it + 1,
            maxCount = 5
        )
    }

    override val actionMoveNextPage: Int =
        R.id.action_protectorOnBoardingFragment_to_protectorSearchOptionMenuFragment
}