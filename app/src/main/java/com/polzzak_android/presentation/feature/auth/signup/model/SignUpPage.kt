package com.polzzak_android.presentation.feature.auth.signup.model

enum class SignUpPage(val progressCount: Int?) {
    ERROR(null),
    TERMS_OF_SERVICE(null),
    SELECT_TYPE(null),
    SELECT_PARENT_TYPE(1),
    SET_NICKNAME(2),
    SET_PROFILE_IMAGE(3)
}
//
//sealed interface SignUpPage {
//    val progressCount: Int?
//    val maxCount: Int
//
//    class Error : SignUpPage {
//        override val progressCount = null
//        override val maxCount = 0
//    }
//
//    class TermsOfService : SignUpPage {
//        override val progressCount = null
//        override val maxCount = 0
//    }
//
//    class SelectType : SignUpPage {
//        override val progressCount = null
//        override val maxCount: Int = 0
//    }
//
//    class SelectParentType(override val progressCount: Int?, override val maxCount: Int) :
//        SignUpPage
//
//    class SetNickName(override val progressCount: Int?, override val maxCount: Int) : SignUpPage
//    class SetProfileImage(override val progressCount: Int?, override val maxCount: Int) : SignUpPage
//}