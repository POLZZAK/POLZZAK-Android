package com.polzzak_android.presentation.common.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed interface MemberType : Parcelable {
    val id: Int
    val label: String

    @Parcelize

    data class Kid(override val id: Int, override val label: String) : MemberType

    @Parcelize

    data class Parent(override val id: Int, override val label: String) : MemberType

    companion object {
        const val KID_TYPE_ID = 1
    }
}