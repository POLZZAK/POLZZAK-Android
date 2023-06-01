package com.polzzak_android.data.repository

import com.polzzak_android.presentation.common.model.MemberType
import com.polzzak_android.presentation.auth.model.SocialLoginType
import com.polzzak_android.data.remote.model.response.CheckNickNameValidationResponse
import com.polzzak_android.data.remote.model.response.SignUpResponse
import com.polzzak_android.data.remote.service.AuthService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class SignUpRepository @Inject constructor(
    private val authService: AuthService
) {
    suspend fun requestCheckNickNameValidation(
        nickName: String
    ): Response<CheckNickNameValidationResponse> {
        return authService.requestCheckNickNameValidation(nickName = nickName)
    }

    suspend fun requestSignUp(
        userName: String,
        memberType: MemberType,
        socialType: SocialLoginType,
        nickName: String,
        profileImagePath: String?,
    ): Response<SignUpResponse> {
        val signUpRequestBody = createSignUpRequestBody(
            userName = userName,
            memberType = memberType,
            socialType = socialType,
            nickName = nickName,
        )
        val profileImageRequestBody = createProfileImageRequestBody(
            profileImagePath = profileImagePath
        )
        return authService.requestSignUp(signUpRequestBody, profileImageRequestBody)
    }

    private fun createSignUpRequestBody(
        userName: String,
        memberType: MemberType,
        socialType: SocialLoginType,
        nickName: String,
    ): RequestBody {
        val jsonObject = JSONObject().apply {
            put("username", userName)
            put("memberType", memberType.toRemoteMemberType().name)
            put("socialType", socialType.toRequestParam())
            put("nickname", nickName)
        }
        return jsonObject.toString().toRequestBody("application/json".toMediaType())
    }

    private fun createProfileImageRequestBody(
        profileImagePath: String?
    ): MultipartBody.Part? {
        val file = profileImagePath?.let { File(profileImagePath) } ?: return null
        val requestBody = file.asRequestBody("image/png".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("profile", profileImagePath, requestBody)
    }

    private fun SocialLoginType.toRequestParam() = when (this) {
        SocialLoginType.KAKAO -> "KAKAO"
        SocialLoginType.GOOGLE -> "GOOGLE"
    }
}