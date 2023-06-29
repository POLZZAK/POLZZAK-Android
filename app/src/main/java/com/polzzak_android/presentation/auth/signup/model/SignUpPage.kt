package com.polzzak_android.presentation.auth.signup.model

enum class SignUpPage(val isHeaderVisible: Boolean, val progressCount: Int) {
    ERROR(false, 0),
    SELECT_TYPE(false, 0),
    SELECT_PARENT_TYPE(true, 1),
    SET_NICKNAME(true, 2),
    SET_PROFILE_IMAGE(true, 3),
    TERMS_OF_SERVICE(true, 4)
}