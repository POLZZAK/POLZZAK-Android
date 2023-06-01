package com.polzzak_android.presentation.common.util

import com.google.gson.Gson
import com.polzzak_android.presentation.common.model.ApiResult
import com.polzzak_android.data.remote.model.response.BaseResponse
import retrofit2.Response

fun <T, R> ApiResult<T>.map(trans: (T?) -> R): ApiResult<R> = when (this) {
    is ApiResult.Success -> ApiResult.success(data = trans(this.data))
    is ApiResult.Loading -> ApiResult.loading()
    is ApiResult.Error -> ApiResult.error(data = trans(this.data), statusCode = statusCode, code = code)
}

inline fun <D, reified T : BaseResponse<D>, R> Response<T>.toApiResult(mapper: (D?) -> R? = { null }): ApiResult<R> {
    val data = if (this.isSuccessful) {
        body()
    } else {
        Gson().fromJson(errorBody()?.string(), T::class.java)
    }

    val rData = mapper(data?.data)
    return if (this.isSuccessful) ApiResult.success(
        data = rData,
    )
    else ApiResult.error(data = rData, statusCode = code(), code = data?.code)
}

fun ApiResult<*>.isLoading() = this is ApiResult.Loading
fun ApiResult<*>.isSuccess() = this is ApiResult.Success
fun ApiResult<*>.isError() = this is ApiResult.Error