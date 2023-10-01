package com.polzzak_android.presentation.feature.auth.signup.model

data class NickNameModel(
    val nickName: String? = null,
    val nickNameState: NickNameValidationState = NickNameValidationState.Unchecked,
    val isEdited: Boolean = false
)