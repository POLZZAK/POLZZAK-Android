package com.polzzak_android.presentation.link.model

import androidx.annotation.StringRes
import com.polzzak_android.R
import com.polzzak_android.presentation.common.model.MemberType


enum class LinkMemberType(@StringRes val stringRes: Int) {
    KID(R.string.common_kid),
    PROTECTOR(R.string.common_protector)
}

fun MemberType?.toLinkRequestMemberTypeOrNull(): LinkMemberType? = when (this) {
    is MemberType.Kid -> LinkMemberType.KID
    is MemberType.Parent -> LinkMemberType.PROTECTOR
    else -> null
}