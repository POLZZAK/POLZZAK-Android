package com.polzzak_android.presentation.common.model

sealed class ApiResult<out T> {
    class Success<T>(val data: T? = null) : ApiResult<T>()
    object Loading : ApiResult<Nothing>()

    /**
     * statusCode 는 http 응답코드, code는 body에 내려오는 코드, 각 케이스는 담당자가 처리
     */
    class Error<T>(val statusCode: Int, val code: Int?, val data: T? = null) : ApiResult<T>()

    companion object {
        fun <T> success(data: T? = null) = Success(data)
        fun loading() = Loading
        fun <T> error(
            data: T? = null,
            statusCode: Int,
            code: Int?
        ) = Error(data = data, statusCode = statusCode, code = code)
    }

    inline fun onSuccess(block: (T?) -> Unit) = apply {
        if (this is Success) {
            block(this.data)
        }
    }

    inline fun onError(
        block: (Int, Int?, T?) -> Unit
    ) = apply {
        if (this is Error) {
            block(
                this.statusCode,
                this.code,
                this.data
            )
        }
    }
}
