package com.polzzak_android.presentation.link

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.TextAppearanceSpan
import androidx.fragment.app.DialogFragment
import com.polzzak_android.R
import com.polzzak_android.presentation.common.model.ButtonCount
import com.polzzak_android.presentation.common.model.CommonButtonModel
import com.polzzak_android.presentation.common.model.CommonDialogContent
import com.polzzak_android.presentation.common.model.CommonDialogModel
import com.polzzak_android.presentation.common.model.DialogStyleType
import com.polzzak_android.presentation.common.widget.CommonDialogHelper
import com.polzzak_android.presentation.common.widget.OnButtonClickListener

//TODO title spannable style 적용(style은 임시로, 현재 다이얼로그는 style이 적용안됨)
class LinkDialogFactory {
    fun createLinkDialog(
        context: Context,
        nickName: String,
        content: String,
        onPositiveButtonClickListener: () -> Unit
    ): DialogFragment =
        CommonDialogHelper.getInstance(
            content = CommonDialogModel(
                type = DialogStyleType.ALERT,
                content = CommonDialogContent(
                    title = createLinkDialogSpannableTitle(
                        context = context,
                        nickName = nickName,
                        content = content
                    ).toString(),
                    body = null
                ),
                button = CommonButtonModel(
                    buttonCount = ButtonCount.TWO,
                    negativeButtonText = "아니요",
                    positiveButtonText = "네, 좋아요!"
                )
            ),
            onCancelListener = {
                object : OnButtonClickListener {
                    override fun setBusinessLogic() {
                        //do nothing
                    }

                    override fun getReturnValue(value: Any) {
                        //do nothing
                    }
                }
            },
            onConfirmListener = {
                object : OnButtonClickListener {
                    override fun setBusinessLogic() {
                        onPositiveButtonClickListener.invoke()
                    }

                    override fun getReturnValue(value: Any) {
                        //do nothing
                    }
                }
            }
        )

    fun createLoadingDialog(context: Context, nickName: String, content: String): DialogFragment =
        CommonDialogHelper.getInstance(
            content = CommonDialogModel(
                type = DialogStyleType.LOADING,
                content = CommonDialogContent(
                    title = createLinkDialogSpannableTitle(
                        context = context,
                        nickName = nickName,
                        content = content
                    ).toString(),
                ),
                button = CommonButtonModel(
                    buttonCount = ButtonCount.ZERO,
                )
            )
        )

    private fun createLinkDialogSpannableTitle(
        context: Context,
        nickName: String,
        content: String
    ): Spannable {
        val nickNameSpannable = SpannableString(nickName).apply {
            val nickNameSpan = TextAppearanceSpan(context, R.style.subtitle_20_600)
            setSpan(nickNameSpan, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        val contentSpannable = SpannableString(content).apply {
            val contentSpan = TextAppearanceSpan(context, R.style.body_13_500)
            setSpan(contentSpan, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return SpannableStringBuilder(nickNameSpannable).append(contentSpannable)
    }
}