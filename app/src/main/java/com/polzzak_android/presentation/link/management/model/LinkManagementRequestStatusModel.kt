package com.polzzak_android.presentation.link.management.model

import com.polzzak_android.data.remote.model.response.LinkRequestStatusResponse

data class LinkManagementRequestStatusModel(
    val itemsVisible: List<Boolean>
)

fun LinkRequestStatusResponse.LinkRequestStatusDto?.toLinkManagementRequestStatusModel() =
    LinkManagementRequestStatusModel(
        itemsVisible = listOf(
            false,
            this?.isReceivedRequestUpdated ?: false,
            this?.isSentRequestUpdated ?: false
        )
    )