package com.polzzak_android.presentation.common.util

fun stampProgressCalculator(curCount: Int, totalCnt: Int): Float {
    return (curCount.toFloat() / totalCnt.toFloat()) * 260f
}