package com.polzzak_android.presentation.feature.link

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.TextAppearanceSpan
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import com.polzzak_android.R
import com.polzzak_android.presentation.common.model.ButtonCount
import com.polzzak_android.presentation.common.model.CommonButtonModel
import com.polzzak_android.presentation.component.dialog.CommonDialogContent
import com.polzzak_android.presentation.component.dialog.CommonDialogModel
import com.polzzak_android.presentation.component.dialog.DialogStyleType
import com.polzzak_android.presentation.component.dialog.CommonDialogHelper
import com.polzzak_android.presentation.component.dialog.FullLoadingDialog
import com.polzzak_android.presentation.component.dialog.OnButtonClickListener

//TODO title spannable style 적용(style은 임시로, 현재 다이얼로그는 style이 적용안됨)
class LinkDialogFactory {
    fun createLinkDialog(
        context: Context,
        nickName: String,
        @StringRes contentStringRes: Int,
        @StringRes positiveButtonStringRes: Int,
        @StringRes negativeButtonStringRes: Int,
        onPositiveButtonClickListener: () -> Unit
    ): DialogFragment =
        CommonDialogHelper.getInstance(
            content = CommonDialogModel(
                type = DialogStyleType.ALERT,
                content = CommonDialogContent(
                    title = createLinkDialogSpannableTitle(
                        context = context,
                        nickName = nickName,
                        content = context.getString(contentStringRes)
                    ).toString(),
                    body = null
                ),
                button = CommonButtonModel(
                    buttonCount = ButtonCount.TWO,
                    negativeButtonText = context.getString(negativeButtonStringRes),
                    positiveButtonText = context.getString(positiveButtonStringRes)
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

    fun createFullLoadingDialog(): DialogFragment = FullLoadingDialog()

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