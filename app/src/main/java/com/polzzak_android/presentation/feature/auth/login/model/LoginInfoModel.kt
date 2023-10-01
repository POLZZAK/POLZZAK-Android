package com.polzzak_android.presentation.feature.auth.login.model

import com.polzzak_android.presentation.feature.auth.model.SocialLoginType
import com.polzzak_android.presentation.feature.auth.model.MemberTypeDetail
import com.polzzak_android.presentation.common.model.MemberType

sealed interface LoginInfoModel {
    data class Login(
        val accessToken: String,
        val memberType: MemberType,
        val socialType: SocialLoginType
    ) : LoginInfoModel

    data class SignUp(
        val userName: String,
        val socialType: SocialLoginType,
        val parentTypes: List<MemberTypeDetail.Parent>
    ) : LoginInfoModel
}
