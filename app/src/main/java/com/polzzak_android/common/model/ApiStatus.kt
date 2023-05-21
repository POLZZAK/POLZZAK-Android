package com.polzzak_android.common.model

//TODO 다른 error 필요 시 추가
enum class ApiStatus {
    OAUTH_AUTHENTICATION_FAIL,
    REQUIRED_REGISTER,
    ACCESS_TOKEN_INVALID,
    REFRESH_TOKEN_INVALID,
    ACCESS_TOKEN_EXPIRED,
    TOKEN_REISSUE_SUCCESS,
    NO_CONTENT,
    UNKNOWN
}