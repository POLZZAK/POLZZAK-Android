package com.polzzak_android.presentation.auth.signup.model

import com.polzzak_android.presentation.common.model.MemberType

data class MemberTypeUiModel(
    val type: MemberType? = null,
    val isParentType: Boolean? = null
)
