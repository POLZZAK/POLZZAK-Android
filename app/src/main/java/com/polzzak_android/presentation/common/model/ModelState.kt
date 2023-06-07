package com.polzzak_android.presentation.common.model

sealed interface ModelState<T> {
    val data: T?

    data class Loading<T>(override val data: T? = null) : ModelState<T>
    data class Success<T>(override val data: T) : ModelState<T>
    data class Error<T>(val exception: Exception, override val data: T? = null) : ModelState<T>
}