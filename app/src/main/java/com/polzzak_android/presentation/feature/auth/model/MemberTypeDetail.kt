package com.polzzak_android.presentation.feature.auth.model

import android.os.Parcelable
import com.polzzak_android.data.remote.model.response.MemberTypeDetailsResponse
import com.polzzak_android.presentation.feature.auth.model.MemberTypeDetail.Companion.KID_TYPE_ID
import kotlinx.parcelize.Parcelize

@Parcelize
sealed interface MemberTypeDetail : Parcelable {
    val id: Int
    val label: String

    @Parcelize

    data class Kid(override val id: Int, override val label: String) : MemberTypeDetail

    @Parcelize

    data class Parent(override val id: Int, override val label: String) : MemberTypeDetail

    companion object {
        const val KID_TYPE_ID = 1
    }
}

fun asMemberTypeDetail(memberTypeDetailResponseData: MemberTypeDetailsResponse.MemberTypeDetailsResponseData.MemberTypeDetailResponseData) =
    when (memberTypeDetailResponseData.memberTypeDetailId) {
        KID_TYPE_ID -> MemberTypeDetail.Kid(
            id = memberTypeDetailResponseData.memberTypeDetailId,
            label = memberTypeDetailResponseData.detail
        )

        else -> MemberTypeDetail.Parent(
            id = memberTypeDetailResponseData.memberTypeDetailId,
            label = memberTypeDetailResponseData.detail
        )
    }