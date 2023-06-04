package com.polzzak_android.presentation.auth.login.model

import com.polzzak_android.presentation.auth.model.SocialLoginType
import com.polzzak_android.presentation.common.model.MemberType

data class LoginInfoUiModel(
    val userName: String? = null,
    val socialType: SocialLoginType? = null,
    val accessToken: String? = null,
    val parentTypes: List<MemberType.Parent>? = null,
    val memberType: MemberType? = null
)
