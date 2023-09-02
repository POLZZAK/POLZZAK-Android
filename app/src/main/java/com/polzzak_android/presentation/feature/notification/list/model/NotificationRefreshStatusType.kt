package com.polzzak_android.presentation.feature.notification.list.model

import androidx.annotation.DrawableRes
import com.polzzak_android.R

sealed interface NotificationRefreshStatusType {
    @get:DrawableRes
    val progressDrawableRes: Int?

    object Disable : NotificationRefreshStatusType {
        override val progressDrawableRes: Int? = null
    }

    object Loading : NotificationRefreshStatusType {
        override val progressDrawableRes: Int = R.drawable.ic_refresh_loading_spinner
    }

    object Normal : NotificationRefreshStatusType {
        override val progressDrawableRes: Int = R.drawable.ic_pulling
    }
}