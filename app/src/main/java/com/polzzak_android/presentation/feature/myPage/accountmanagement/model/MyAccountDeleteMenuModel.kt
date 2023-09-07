package com.polzzak_android.presentation.feature.myPage.accountmanagement.model

data class MyAccountDeleteMenuModel(
    val isCheckedDeleteSocialAccountData: Boolean = false,
    val isCheckedDeleteLink: Boolean = false,
    val isCheckedDeletePoint: Boolean = false,
    val isCheckedDeleteStampAndCoupon: Boolean = false
) {
    fun isAllChecked() =
        isCheckedDeleteLink && isCheckedDeletePoint && isCheckedDeleteSocialAccountData && isCheckedDeleteStampAndCoupon
}
