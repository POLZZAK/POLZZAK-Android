package com.polzzak_android.presentation.feature.auth.signup.model

sealed class NickNameValidationState {
    object Unchecked : NickNameValidationState()
    object Valid : NickNameValidationState()
    object Invalid : NickNameValidationState()
    class Error(val exception: Exception) : NickNameValidationState()
}