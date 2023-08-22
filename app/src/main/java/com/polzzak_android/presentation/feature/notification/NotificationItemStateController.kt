package com.polzzak_android.presentation.feature.notification

interface NotificationItemStateController {
    fun setHorizontalScrollPosition(id: Int, position: Int)
    fun getHorizontalScrollPosition(id: Int): Int
    fun getIsRefreshedSuccess(): Boolean
}