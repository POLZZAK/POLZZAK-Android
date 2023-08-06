package com.polzzak_android.presentation.feature.notification.base

import androidx.fragment.app.viewModels
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentNotificationBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.feature.notification.NotificationViewModel

abstract class BaseNotificationFragment : BaseFragment<FragmentNotificationBinding>() {
    override val layoutResId: Int = R.layout.fragment_notification
    private val notificationViewModel by viewModels<NotificationViewModel>()
}