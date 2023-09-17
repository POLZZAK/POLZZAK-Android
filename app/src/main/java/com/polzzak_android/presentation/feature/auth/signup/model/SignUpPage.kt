package com.polzzak_android.presentation.feature.auth.signup.model

sealed interface SignUpPage {
    val progressCount: Int?
    val maxCount: Int

    class Error : SignUpPage {
        override val progressCount = null
        override val maxCount = 0
    }

    class TermsOfService : SignUpPage {
        override val progressCount = null
        override val maxCount = 0
    }

    class SelectType : SignUpPage {
        override val progressCount = null
        override val maxCount: Int = 0
    }

    class SelectParentType(override val progressCount: Int, override val maxCount: Int) :
        SignUpPage

    class SetNickName(override val progressCount: Int, override val maxCount: Int) : SignUpPage

    class SetProfileImage(override val progressCount: Int, override val maxCount: Int) : SignUpPage
}