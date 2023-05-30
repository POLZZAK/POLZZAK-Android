package com.polzzak_android.presentation.signup.model

data class NickNameUiModel(
    val nickName: String? = null,
    val nickNameState: NickNameValidationState = NickNameValidationState.UNCHECKED
)