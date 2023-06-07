package com.polzzak_android.presentation.auth.login.model

import com.polzzak_android.presentation.auth.model.SocialLoginType
import com.polzzak_android.presentation.auth.model.MemberTypeDetail
import com.polzzak_android.presentation.common.model.MemberType

sealed interface LoginInfoUiModel {
    data class Login(val accessToken: String, val memberType: MemberType) : LoginInfoUiModel
    data class SignUp(
        val userName: String,
        val socialType: SocialLoginType,
        val parentTypes: List<MemberTypeDetail.Parent>
    ) : LoginInfoUiModel
}
