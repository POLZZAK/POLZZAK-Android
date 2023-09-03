package com.polzzak_android.presentation.common.util

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.TextAppearanceSpan

fun createStyledSpannable(
    context: Context,
    fullText: String,
    targetText: String,
    fullTextStyleRes: Int,
    targetTextStyleRes: Int,
): SpannableString {
    // 전체 텍스트
    val fullSpannable = SpannableString(fullText)
    val fullTextSpan = TextAppearanceSpan(context, fullTextStyleRes)
    fullSpannable.setSpan(fullTextSpan, 0, fullText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

    // 변경할 텍스트
    val targetTextSpan = TextAppearanceSpan(context, targetTextStyleRes)
    val startIndex = fullText.indexOf(targetText)
    val endIndex = startIndex + targetText.length

    fullSpannable.setSpan(targetTextSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

    return fullSpannable
}

fun createColoredSpannable(
    context: Context,
    fullText: String,
    targetText: String,
    fullTextColor: Int,
    targetTextColor: Int,
): SpannableString {
    // 전체 텍스트
    val fullSpannable = SpannableString(fullText)
    val fullTextSpan = ForegroundColorSpan(context.getColor(fullTextColor))

    // 변경할 텍스트
    val targetTextSpan = ForegroundColorSpan(context.getColor(targetTextColor))
    val startIndex = fullText.indexOf(targetText)
    val endIndex = startIndex + targetText.length

    fullSpannable.setSpan(fullTextSpan, 0, startIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    fullSpannable.setSpan(targetTextSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

    return fullSpannable
}