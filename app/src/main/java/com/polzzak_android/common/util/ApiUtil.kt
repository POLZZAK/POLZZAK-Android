package com.polzzak_android.common.util

import com.polzzak_android.common.model.ApiResult
import com.polzzak_android.common.model.ApiStatus


fun Int?.toApiStatus() = when (this) {
    411 -> ApiStatus.OAUTH_AUTHENTICATION_FAIL
    412 -> ApiStatus.REQUIRED_REGISTER
    431 -> ApiStatus.ACCESS_TOKEN_INVALID
    432 -> ApiStatus.REFRESH_TOKEN_INVALID
    433 -> ApiStatus.ACCESS_TOKEN_EXPIRED
    434 -> ApiStatus.TOKEN_REISSUE_SUCCESS
    else -> ApiStatus.UNKNOWN
}

fun <T, R> ApiResult<T>.map(trans: (T?) -> R) = when (this) {
    is ApiResult.Success -> ApiResult.Success(data = trans(data), status = status)
    is ApiResult.Loading -> ApiResult.Loading(data = trans(data))
    is ApiResult.Error -> ApiResult.Error(data = trans(data), status = status)
}