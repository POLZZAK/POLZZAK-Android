package com.polzzak_android.data.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.polzzak_android.presentation.feature.auth.model.SocialLoginType
import javax.inject.Inject

class LastSocialLoginRepository @Inject constructor(context: Context) {
    private val sharedPref = context.getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_PRIVATE)
    fun loadLastSocialLoginType(): SocialLoginType? =
        sharedPref.getString(LAST_SOCIAL_LOGIN_KEY, null)?.toSocialLoginType()


    fun saveLastLogin(socialLoginType: SocialLoginType) {
        val editor = sharedPref.edit()
        editor.putString(LAST_SOCIAL_LOGIN_KEY, socialLoginType.toLastSocialLoginString())
        editor.apply()
    }

    private fun SocialLoginType.toLastSocialLoginString() = when (this) {
        SocialLoginType.GOOGLE -> "google"
        SocialLoginType.KAKAO -> "kakao"
    }

    private fun String.toSocialLoginType() = when (this) {
        "google" -> SocialLoginType.GOOGLE
        "kakao" -> SocialLoginType.KAKAO
        else -> null
    }

    companion object {
        private const val SHARED_PREFERENCE_NAME = "last_social_login_shared_pref"
        private const val LAST_SOCIAL_LOGIN_KEY = "last_social_login"
    }
}