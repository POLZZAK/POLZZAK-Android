package com.polzzak_android.presentation.feature.link.management.model

import androidx.annotation.StringRes
import com.polzzak_android.R

enum class LinkManagementMainTabTypeModel(@StringRes val titleStringRes: Int) {
    LINKED(R.string.link_management_tab_link),
    RECEIVED(R.string.link_management_tab_received),
    SENT(R.string.link_management_tab_sent)
}