package com.polzzak_android.presentation.feature.link.model

import androidx.annotation.StringRes
import com.polzzak_android.R

sealed interface LinkEventType {
    val linkUserModel: LinkUserModel

    sealed interface DialogType : LinkEventType {
        @get:StringRes
        val contentStrRes: Int

        @get:StringRes
        val positiveBtnStrRes: Int

        class DeleteLink(override val linkUserModel: LinkUserModel) : DialogType {
            override val contentStrRes: Int = R.string.link_dialog_delete_link_content
            override val positiveBtnStrRes: Int = R.string.link_dialog_btn_positive_delete_link
        }

        class ApproveRequest(override val linkUserModel: LinkUserModel) : DialogType {
            override val contentStrRes: Int = R.string.link_dialog_approve_request_content
            override val positiveBtnStrRes: Int = R.string.link_dialog_btn_positive_approve_request
        }

        class RejectRequest(override val linkUserModel: LinkUserModel) : DialogType {
            override val contentStrRes: Int = R.string.link_dialog_reject_request_content
            override val positiveBtnStrRes: Int = R.string.link_dialog_btn_positive_reject_request
        }

        class CancelRequest(override val linkUserModel: LinkUserModel) : DialogType {
            override val contentStrRes: Int = R.string.link_dialog_cancel_request_content
            override val positiveBtnStrRes: Int = R.string.link_dialog_btn_positive_cancel_request
        }

        class RequestLink(override val linkUserModel: LinkUserModel) : DialogType {
            override val contentStrRes: Int = R.string.link_dialog_request_content
            override val positiveBtnStrRes: Int = R.string.link_dialog_btn_positive_request_link
        }
    }

    class CancelRequest(override val linkUserModel: LinkUserModel) : LinkEventType
}