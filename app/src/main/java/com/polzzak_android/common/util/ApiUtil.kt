package com.polzzak_android.common.util

import com.google.gson.Gson
import com.polzzak_android.common.model.ApiResult
import com.polzzak_android.data.remote.model.response.BaseResponse
import retrofit2.Response

fun <T, R> ApiResult<T>.map(trans: (T?) -> R) = when (this) {
    is ApiResult.Success -> ApiResult.Success(data = trans(data))
    is ApiResult.Loading -> ApiResult.Loading(data = trans(data))
    is ApiResult.Error -> ApiResult.Error(data = trans(data), statusCode = statusCode, code = code)
}

inline fun <D, reified T : BaseResponse<D>, R> Response<T>.toApiResult(mapper: (D?) -> R? = { null }): ApiResult<R> {
    val data =
        if (this.isSuccessful) body() else Gson().fromJson(errorBody()?.string(), T::class.java)
    val rData = mapper(data?.data)
    return if (this.isSuccessful) ApiResult.Success(
        data = rData,
    )
    else ApiResult.Error(data = rData, statusCode = code(), code = data?.code)
}

fun ApiResult<*>.isLoading() = this is ApiResult.Loading
fun ApiResult<*>.isSuccess() = this is ApiResult.Success
fun ApiResult<*>.isError() = this is ApiResult.Error