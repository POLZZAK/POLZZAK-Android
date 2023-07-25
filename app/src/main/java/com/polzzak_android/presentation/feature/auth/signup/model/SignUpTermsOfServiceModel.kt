package com.polzzak_android.presentation.feature.auth.signup.model

data class SignUpTermsOfServiceModel(
    val isCheckedService: Boolean = false,
    val isCheckedPrivacy: Boolean = false
) {
    enum class ClickModel {
        SERVICE,
        PRIVACY,
        ALL
    }
}