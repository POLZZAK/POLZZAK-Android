package com.polzzak_android.data.repository

import android.net.Uri
import com.polzzak_android.data.remote.model.ApiResult
import com.polzzak_android.data.remote.model.response.ProfileDto
import com.polzzak_android.data.remote.model.response.UserInfoDto
import com.polzzak_android.data.remote.service.UserService
import com.polzzak_android.data.remote.util.createHeaderAuthorization
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

class UserRepository @Inject constructor(
    private val userService: UserService
) {
    suspend fun requestUser(accessToken: String): ApiResult<UserInfoDto> =
        requestCatching {
            val authorization = createHeaderAuthorization(accessToken = accessToken)
            userService.requestUserInfo(authorization = authorization)
        }

    suspend fun requestProfile(accessToken: String): ApiResult<ProfileDto> =
        requestCatching {
            val authorization = createHeaderAuthorization(accessToken = accessToken)
            userService.requestProfile(authorization = authorization)
        }

    suspend fun updateUserInfo(accessToken: String, profileInfo: ProfileDto, profileImagePath: String?): ApiResult<Unit> =
        requestCatching {
            val authorization = createHeaderAuthorization(accessToken = accessToken)
            val profilePart = createSignUpPart(profile = profileInfo)
            val profileImagePart = createProfileImagePart(profileImagePath = profileImagePath)
            val multipartBody = MultipartBody.Builder()
                .addPart(profilePart)
                .apply {
                    profileImagePart?.let { addPart(it) }
                }.build()
            val contentType = "multipart/form-data; charset=utf-8; boundary=${multipartBody.boundary}"
            userService.updateUserInfo(authorization = authorization, contentType = contentType, profile = multipartBody)
        }

    private fun createSignUpPart(
        profile: ProfileDto
    ): MultipartBody.Part {
        val jsonObject = JSONObject().apply {
            put("memberId", profile.memberId)
            put("nickname", profile.nickName)
            put("memberPoint", profile.memberPoint)
            put("memberType", profile.memberType)
            put("profileUrl", profile.profileUrl)
            put("familyCount", profile.linkedUser)
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

    suspend fun updateUserNickname(accessToken: String, nickname: String): ApiResult<Unit> =
        requestCatching {
            val authorization = createHeaderAuthorization(accessToken = accessToken)
            val contentType = "application/json;charset=UTF-8"
            userService.updateUserNickname(authorization = authorization, contentType = contentType, nickname = nickname)
        }
}