package com.polzzak_android.presentation.feature.onboarding.model

import androidx.annotation.StringRes

data class OnBoardingPageModel(
    @StringRes val titleStringRes: Int,
    @StringRes val contentStringRes: Int,
    val progress: Int,
    val maxCount: Int
)
