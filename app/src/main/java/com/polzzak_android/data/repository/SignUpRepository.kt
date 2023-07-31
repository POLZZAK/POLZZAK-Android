package com.polzzak_android.data.repository

import com.polzzak_android.data.remote.model.ApiResult
import com.polzzak_android.data.remote.model.response.SignUpResponse
import com.polzzak_android.data.remote.service.AuthService
import com.polzzak_android.data.remote.util.requestCatching
import com.polzzak_android.presentation.feature.auth.model.SocialLoginType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import javax.inject.Inject

class SignUpRepository @Inject constructor(
    private val authService: AuthService
) {
    suspend fun requestCheckNickNameValidation(
        nickName: String
    ): ApiResult<Nothing?> = requestCatching {
        authService.requestCheckNickNameValidation(nickName = nickName)
    }

    suspend fun requestSignUp(
        userName: String,
        memberTypeId: Int,
        socialType: SocialLoginType,
        nickName: String,
        profileImagePath: String?,
    ): ApiResult<SignUpResponse.SignUpResponseData> = requestCatching {
        val signUpPart = createSignUpPart(
            userName = userName,
            memberTypeId = memberTypeId,
            socialType = socialType,
            nickName = nickName,
        )
        val profileImagePart = createProfileImagePart(profileImagePath = profileImagePath)
        val multipartBody = MultipartBody.Builder()
            .addPart(signUpPart)
            .apply {
                profileImagePart?.let { addPart(it) }
            }.build()
        val contentType = "multipart/form-data; charset=utf-8; boundary=${multipartBody.boundary}"
        authService.requestSignUp(contentType, multipartBody)
    }

    private fun createSignUpPart(
        userName: String,
        memberTypeId: Int,
        socialType: SocialLoginType,
        nickName: String,
    ): MultipartBody.Part {
        val jsonObject = JSONObject().apply {
            put("username", userName)
            put("socialType", socialType.toRequestParam())
            put("memberTypeDetailId", memberTypeId)
            put("nickname", nickName)
        }
        val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaType())
        return MultipartBody.Part.createFormData("registerRequest", "registerRequest", requestBody)
    }

    private fun createProfileImagePart(
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