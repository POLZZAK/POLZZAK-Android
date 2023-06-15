package com.polzzak_android.presentation.link.model

data class LinkRequestUserModel(
    val user: LinkUserModel? = null,
    val status: LinkUserStatusModel = LinkUserStatusModel.GUIDE
)
