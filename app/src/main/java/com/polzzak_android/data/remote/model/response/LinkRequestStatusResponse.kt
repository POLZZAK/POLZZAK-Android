package com.polzzak_android.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class LinkRequestStatusResponse(
    override val code: Int?,
    override val messages: List<String>?,
    override val data: LinkRequestStatusDto?
) : BaseResponse<LinkRequestStatusResponse.LinkRequestStatusDto> {
    data class LinkRequestStatusDto(
        @SerializedName("isFamilyReceived")
        val isReceivedUpdated: Boolean,
        @SerializedName("isFamilySent")
        val isFamilySentUpdated: Boolean
    )
}
