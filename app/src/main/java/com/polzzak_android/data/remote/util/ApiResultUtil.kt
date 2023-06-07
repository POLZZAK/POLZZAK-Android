package com.polzzak_android.data.remote.util

import com.google.gson.Gson
import com.polzzak_android.data.remote.model.ApiException
import com.polzzak_android.data.remote.model.ApiResult
import com.polzzak_android.data.remote.model.response.BaseResponse
import retrofit2.Response

@JvmName("requestCatching")
inline fun <T> requestCatching(block: () -> Response<T>): ApiResult<T> {
    return try {
        val response = block.invoke()
        if (response.isSuccessful) {
            ApiResult.Success(data = response.body())
        } else {
            val error = asApiError(response.code())
            ApiResult.Error(exception = error)
        }
    } catch (e: Exception) {
        ApiResult.Error(exception = e)
    }
}

@JvmName("requestCatchingBaseResponse")
inline fun <reified T : BaseResponse<R>, R> requestCatching(block: () -> Response<T>): ApiResult<R> {
    return try {
        val response = block.invoke()
        response.toApiResult()
    } catch (e: Exception) {
        ApiResult.Error(exception = e)
    }
}


inline fun <reified T : BaseResponse<R>, R> Response<T>.toApiResult(): ApiResult<R> {
    val data = if (this.isSuccessful) {
        body()
    } else {
        Gson().fromJson(errorBody()?.string(), T::class.java)
    }
    val apiError = asApiError(code = data?.code ?: code())
    return if (this.isSuccessful) ApiResult.success(
        data = data?.data,
    )
    else ApiResult.error(exception = apiError, data = data?.data)
}

fun asApiError(code: Int): Exception {
    return when (code) {
        400 -> ApiException.BadRequest()
        401 -> ApiException.Unauthorized()
        403 -> ApiException.Forbidden()
        410 -> ApiException.RequestResourceNotValid()
        411 -> ApiException.OauthAuthenticationFail()
        412 -> ApiException.RequiredRegister()
        431 -> ApiException.AccessTokenInvalid()
        432 -> ApiException.RefreshTokenInvalid()
        433 -> ApiException.AccessTokenExpired()
        434 -> ApiException.TokenReissueSuccess()
        435 -> ApiException.TokenUnauthorized()
        450 -> ApiException.FileUploadFail()
        451 -> ApiException.FindFileFail()
        452 -> ApiException.DeleteFileFail()
        else -> ApiException.UnknownError()
    }
}

