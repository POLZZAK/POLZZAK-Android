package com.polzzak_android.data.remote.model.response

data class CheckNickNameValidationResponse(
    override val code: Int?,
    override val messages: List<String>?,
    override val data: Nothing?
) : BaseResponse<Nothing?>
