package com.polzzak_android.data.remote.model

//TODO 네트워크, 서버 에러 처리 필요하면 추가
sealed class ApiException(override val message: String? = null) : Exception() {
    class BadRequest : ApiException(message = "Bad request / Request is invalid")
    class Unauthorized : ApiException(message = "Token is invalid / Unauthenticated Access")
    class Forbidden : ApiException(message = "Permission is invalid")
    class RequestResourceNotValid : ApiException(message = "Request resource is invalid")
    class OauthAuthenticationFail : ApiException(message = "Social Login failed")
    class RequiredRegister : ApiException(message = "Register is required")
    class TargetNotExist : ApiException(message = "Target is not exist")
    class AccessTokenInvalid : ApiException(message = "AccessToken is invalid")
    class RefreshTokenInvalid : ApiException(message = "RefreshToken is invalid")
    class AccessTokenExpired : ApiException(message = "Success token reissue")
    class TokenReissueSuccess : ApiException(message = "Success token reissue")
    class TokenUnauthorized : ApiException(message = "Request not authorized")
    class FileUploadFail : ApiException(message = "Failed to upload file")
    class FindFileFail : ApiException(message = "Failed to locate file")
    class DeleteFileFail : ApiException(message = "ailed to delete file")
    class UnknownError : ApiException(message = "unknown error")
}




