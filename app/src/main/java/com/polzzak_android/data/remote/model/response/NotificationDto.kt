package com.polzzak_android.data.remote.model.response

data class NotificationDto(
    val id: Int?,
    val type: String?,
    val status: String?,
    val title: String?,
    val message: String?,
    val sender: Sender?,
    val link: String?,
    val requestFamilyId: Int?,
    val createdDate: String?
) {
    data class Sender(
        val id: Int?,
        val nickName: String?,
        val profileUrl: String?
    )
}