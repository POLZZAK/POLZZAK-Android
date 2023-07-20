package com.polzzak_android.presentation.link.model

import com.polzzak_android.data.remote.model.response.UserInfoDto
import com.polzzak_android.presentation.common.model.asMemberTypeOrNull

sealed interface LinkRequestUserModel {
    val user: LinkUserModel?

    data class Guide(val targetLinkMemberType: LinkMemberType) : LinkRequestUserModel {
        override val user: LinkUserModel? = null
    }

    data class Empty(val nickName: String) : LinkRequestUserModel {
        override val user: LinkUserModel? = null
    }

    data class Normal(override val user: LinkUserModel) : LinkRequestUserModel
    data class Sent(override val user: LinkUserModel) : LinkRequestUserModel
    data class Linked(override val user: LinkUserModel) : LinkRequestUserModel

    data class Received(override val user: LinkUserModel) : LinkRequestUserModel
}

fun UserInfoDto?.toLinkRequestUserModel(
    nickName: String,
    linkMemberType: LinkMemberType
): LinkRequestUserModel {
    this ?: return LinkRequestUserModel.Empty(nickName = nickName)
    val searchRequestMemberType =
        asMemberTypeOrNull(memberTypeResponseData = this.memberType).toLinkRequestMemberTypeOrNull()
    val userModel = this.toLinkUserModel()
    return when {
        (linkMemberType == searchRequestMemberType) -> LinkRequestUserModel.Empty(nickName = nickName)
        this.status == "SENT" -> LinkRequestUserModel.Sent(user = userModel)
        this.status == "APPROVE" -> LinkRequestUserModel.Linked(user = userModel)
        this.status == "NONE" -> LinkRequestUserModel.Normal(user = userModel)
        this.status == "RECEIVED" -> LinkRequestUserModel.Received(user = userModel)
        else -> LinkRequestUserModel.Empty(nickName = nickName)
    }
}