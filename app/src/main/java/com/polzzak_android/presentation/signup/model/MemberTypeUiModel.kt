package com.polzzak_android.presentation.signup.model

import com.polzzak_android.common.model.MemberType

data class MemberTypeUiModel(
    val type: MemberType? = null,
    val isParentType: Boolean? = null
)
