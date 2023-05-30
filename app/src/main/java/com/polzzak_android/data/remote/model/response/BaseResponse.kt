package com.polzzak_android.data.remote.model.response

interface BaseResponse<T> {
    val code: Int?
    val messages: List<String>?
    val data: T?
}