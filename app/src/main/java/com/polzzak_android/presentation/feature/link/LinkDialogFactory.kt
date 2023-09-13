package com.polzzak_android.presentation.feature.link

import android.content.Context
import android.text.Spannable
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import com.polzzak_android.R
import com.polzzak_android.presentation.common.model.ButtonCount
import com.polzzak_android.presentation.common.model.CommonButtonModel
import com.polzzak_android.presentation.common.util.SpannableBuilder
import com.polzzak_android.presentation.component.dialog.CommonDialogContent
import com.polzzak_android.presentation.component.dialog.CommonDialogHelper
import com.polzzak_android.presentation.component.dialog.CommonDialogModel
import com.polzzak_android.presentation.component.dialog.DialogStyleType
import com.polzzak_android.presentation.component.dialog.FullLoadingDialog
import com.polzzak_android.presentation.component.dialog.OnButtonClickListener

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
                    ),
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
                    ),
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
        return SpannableBuilder.build(context) {
            span(text = nickName, textColor = R.color.gray_700, style = R.style.body_18_700)
            span(text = content, textColor = R.color.gray_700, style = R.style.body_18_500)
        }
    }
}