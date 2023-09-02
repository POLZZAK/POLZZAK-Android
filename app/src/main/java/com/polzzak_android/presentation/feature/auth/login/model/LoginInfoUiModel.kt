package com.polzzak_android.presentation.feature.auth.login.model

import com.polzzak_android.presentation.feature.auth.model.SocialLoginType
import com.polzzak_android.presentation.feature.auth.model.MemberTypeDetail
import com.polzzak_android.presentation.common.model.MemberType

sealed interface LoginInfoUiModel {
    data class Login(
        val accessToken: String,
        val memberType: MemberType,
        val socialType: SocialLoginType
    ) : LoginInfoUiModel

    data class SignUp(
        val userName: String,
        val socialType: SocialLoginType,
        val parentTypes: List<MemberTypeDetail.Parent>
    ) : LoginInfoUiModel
}
