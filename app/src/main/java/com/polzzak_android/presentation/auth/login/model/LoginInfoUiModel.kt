package com.polzzak_android.presentation.auth.login.model

import com.polzzak_android.presentation.auth.model.SocialLoginType
import com.polzzak_android.presentation.auth.signup.model.MemberTypeDetail

data class LoginInfoUiModel(
    val userName: String? = null,
    val socialType: SocialLoginType? = null,
    val accessToken: String? = null,
    val parentTypes: List<MemberTypeDetail.Parent>? = null,
    val memberType: MemberTypeDetail? = null
)
