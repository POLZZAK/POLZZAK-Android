package com.polzzak_android.presentation.common.util

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.TextAppearanceSpan
import android.text.style.UnderlineSpan
import androidx.annotation.ColorRes
import androidx.annotation.StyleRes
import androidx.core.text.set
import androidx.core.text.toSpannable

/**
 * [Spannable]을 쉽게 만들 수 있게 해주는 유틸 클래스.
 *
 * 사용 예:
 * ```
 * val spannable = SpannableBuilder.build(context) {
 *      span(text = "안녕", textColor = R.color.Red, style = R.style.title)
 *      span(text = "하세요", textColor = R.color.Bule, underline = true)
 * }
 * ```
 */
class SpannableBuilder private constructor(
    private val context: Context,
    private val spannableBuilder: SpannableStringBuilder = SpannableStringBuilder()
) {
    fun span(
        text: String,
        @StyleRes style: Int = -1,
        @ColorRes textColor: Int = -1,
        @ColorRes backgroundColor: Int = -1,
        underline: Boolean = false
    ) {
        val startIndex = spannableBuilder.length
        val endIndex = startIndex + text.length

        spannableBuilder.append(text)

        if (style != -1) {
            spannableBuilder[startIndex..endIndex] = TextAppearanceSpan(context, style)
        }
        if (textColor != -1) {
            spannableBuilder[startIndex..endIndex] = ForegroundColorSpan(context.getColor(textColor))
        }
        if (backgroundColor != -1) {
            spannableBuilder[startIndex..endIndex] = BackgroundColorSpan(context.getColor(backgroundColor))
        }
        if (underline) {
            spannableBuilder[startIndex..endIndex] = UnderlineSpan()
        }
    }

    companion object {
        fun build(context: Context, block: SpannableBuilder.() -> Unit): Spannable {
            return SpannableBuilder(context)
                .apply { block() }
                .spannableBuilder.toSpannable()
        }
    }
}