package com.polzzak_android.data.remote.model.response

data class NotificationsResponse(
    override val code: Int?,
    override val messages: List<String>?,
    override val data: NotificationsDto?
) : BaseResponse<NotificationsDto>