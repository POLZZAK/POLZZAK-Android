package com.polzzak_android.common.model

sealed class ApiResult<T>(open val data: T?) {
    class Success<T>(override val data: T) : ApiResult<T>(data)
    class Loading<T>(override val data: T? = null) : ApiResult<T>(data)
    class Error<T>(override val data: T? = null, val errorType: ApiError) : ApiResult<T>(data)
}