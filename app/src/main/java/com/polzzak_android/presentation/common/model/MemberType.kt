package com.polzzak_android.presentation.common.model

import com.polzzak_android.data.remote.model.response.MemberTypeResponseData

sealed interface MemberType {
    val label: String

    class Kid(override val label: String) : MemberType
    class Parent(override val label: String) : MemberType
}

fun asMemberTypeOrNull(memberTypeResponseData: MemberTypeResponseData): MemberType? {
    memberTypeResponseData.let {
        return when (it.name) {
            "GUARDIAN" -> MemberType.Parent(label = it.detail)
            "KID" -> MemberType.Kid(label = it.detail)
            else -> null
        }
    }
}