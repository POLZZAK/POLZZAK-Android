package com.polzzak_android.presentation.link.model

import com.polzzak_android.data.remote.model.response.UserInfoDto
import com.polzzak_android.presentation.common.model.MemberType
import com.polzzak_android.presentation.common.model.asMemberTypeOrNull

//TODO RECEIVED 아이템 구현
enum class LinkUserStatusModel {
    GUIDE, NORMAL, EMPTY, LINKED, SENT, RECEIVED
}

fun UserInfoDto?.toLinkUserStatusModel(isKid: Boolean): LinkUserStatusModel {
    this ?: return LinkUserStatusModel.EMPTY
    val memberType = asMemberTypeOrNull(memberTypeResponseData = this.memberType)
    return when {
        isKid == (memberType is MemberType.Kid) -> LinkUserStatusModel.EMPTY
        this.status == "RECEIVED" -> LinkUserStatusModel.RECEIVED
        this.status == "SENT" -> LinkUserStatusModel.SENT
        this.status == "APPROVE" -> LinkUserStatusModel.LINKED
        this.status == "NONE" -> LinkUserStatusModel.NORMAL
        else -> LinkUserStatusModel.EMPTY
    }
}