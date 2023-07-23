package com.polzzak_android.presentation.auth.signup.model

enum class SignUpPage(val progressCount: Int?) {
    ERROR(null),
    TERMS_OF_SERVICE(null),
    SELECT_TYPE(null),
    SELECT_PARENT_TYPE(1),
    SET_NICKNAME(2),
    SET_PROFILE_IMAGE(3)
}