package com.polzzak_android.data.remote.model

sealed class ApiResult<out T>(open val data: T?) {

    data class Success<T>(override val data: T? = null) : ApiResult<T>(data)
    data class Error<T>(val exception: Exception, override val data: T? = null) : ApiResult<T>(data)

    companion object {
        fun <T> success(data: T? = null) = Success(data)
        fun <T> error(
            exception: Exception,
            data: T? = null
        ) = Error(exception = exception, data = data)
    }

    inline fun onSuccess(block: (T?) -> Unit) = apply {
        if (this is Success) {
            block(this.data)
        }
    }

    inline fun onError(
        block: (Exception, T?) -> Unit
    ) = apply {
        if (this is Error) {
            block(
                this.exception,
                this.data
            )
        }
    }

    fun isSuccess() = this is Success
    fun isError() = this is Error
}
