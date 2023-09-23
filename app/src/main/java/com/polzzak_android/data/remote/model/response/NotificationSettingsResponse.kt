package com.polzzak_android.data.remote.model.response

data class NotificationSettingsResponse(
    override val code: Int?,
    override val messages: List<String>?,
    override val data: Map<String, Boolean?>?
) : BaseResponse<Map<String, Boolean?>>