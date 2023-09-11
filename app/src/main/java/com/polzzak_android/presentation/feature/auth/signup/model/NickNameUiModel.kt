package com.polzzak_android.presentation.feature.auth.signup.model

data class NickNameUiModel(
    val nickName: String? = null,
    val nickNameState: NickNameValidationState = NickNameValidationState.Unchecked
)