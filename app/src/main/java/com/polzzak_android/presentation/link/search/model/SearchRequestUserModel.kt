package com.polzzak_android.presentation.link.search.model

data class SearchRequestUserModel(
    val userId: Int,
    val nickName: String,
    val profileUrl: String?,
    val status: SearchUserStatusModel
)
