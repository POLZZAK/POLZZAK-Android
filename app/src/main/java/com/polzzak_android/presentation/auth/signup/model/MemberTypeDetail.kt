package com.polzzak_android.presentation.auth.signup.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed interface MemberTypeDetail : Parcelable {
    val id: Int
    val label: String

    @Parcelize

    data class Kid(override val id: Int, override val label: String) : MemberTypeDetail

    @Parcelize

    data class Parent(override val id: Int, override val label: String) : MemberTypeDetail

    companion object {
        const val KID_TYPE_ID = 1
    }
}