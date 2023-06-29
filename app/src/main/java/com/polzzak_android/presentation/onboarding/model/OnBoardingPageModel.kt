package com.polzzak_android.presentation.onboarding.model

data class OnBoardingPageModel(
    val title: String,
    val content: String,
    val progress: Int,
    val maxCount: Int
)
