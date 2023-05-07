package com.polzzak_android.repository

import com.polzzak_android.common.model.SocialLoginType
import com.polzzak_android.common.model.UserInfo
import javax.inject.Inject

class LoginRepository @Inject constructor() {
    fun requestUserInfo(id: String, loginType: SocialLoginType) = Result.success(mockData)
}

private val mockData = UserInfo(nickName = "test1", userType = "google", profileUrl = null)