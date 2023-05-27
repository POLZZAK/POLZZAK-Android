package com.polzzak_android.common.model

sealed class ApiResult<T>(open val data: T?) {
    class Success<T>(override val data: T? = null) : ApiResult<T>(data)
    class Loading<T>(override val data: T? = null) : ApiResult<T>(data)

    //statusCode 는 http 응답코드, code는 body에 내려오는 코드, 각 케이스는 담당자가 처리
    class Error<T>(override val data: T? = null, val statusCode: Int, val code: Int?) : ApiResult<T>(data)
}
