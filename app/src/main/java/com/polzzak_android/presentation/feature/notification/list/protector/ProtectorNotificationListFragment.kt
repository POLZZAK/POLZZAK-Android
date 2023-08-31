package com.polzzak_android.presentation.feature.notification.list.protector

import com.polzzak_android.R
import com.polzzak_android.presentation.common.model.MemberType
import com.polzzak_android.presentation.feature.notification.list.base.BaseNotificationListFragment

class ProtectorNotificationListFragment : BaseNotificationListFragment() {
    override val actionToSettingFragment: Int =
        R.id.action_protectorNotificationFragment_to_notificationSettingFragment
    override val memberType: MemberType = MemberType.Parent("")
}