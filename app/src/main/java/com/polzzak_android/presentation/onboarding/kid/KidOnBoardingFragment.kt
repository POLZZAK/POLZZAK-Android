package com.polzzak_android.presentation.onboarding.kid

import com.polzzak_android.R
import com.polzzak_android.presentation.onboarding.base.BaseOnBoardingFragment
import com.polzzak_android.presentation.onboarding.model.OnBoardingPageModel

class KidOnBoardingFragment : BaseOnBoardingFragment() {
    private val titleStringResList = listOf(
        R.string.on_boarding_kid_first_title,
        R.string.on_boarding_kid_second_title,
        R.string.on_boarding_kid_third_title,
        R.string.on_boarding_kid_fourth_title,
        R.string.on_boarding_kid_fifth_title
    )
    private val contentStringResList = listOf(
        R.string.on_boarding_kid_first_content,
        R.string.on_boarding_kid_second_content,
        R.string.on_boarding_kid_third_content,
        R.string.on_boarding_kid_fourth_content,
        R.string.on_boarding_kid_fifth_content
    )
    override val pageData = List(5) {
        OnBoardingPageModel(
            titleStringRes = titleStringResList[it],
            contentStringRes = contentStringResList[it]
        )
    }
}