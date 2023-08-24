package com.polzzak_android.presentation.feature.notification.list.kid

import com.polzzak_android.R
import com.polzzak_android.presentation.common.model.MemberType
import com.polzzak_android.presentation.feature.notification.list.base.BaseNotificationListFragment

class KidNotificationListFragment: BaseNotificationListFragment(){
    override val actionToSettingFragment: Int = R.id.action_kidNotificationFragment_to_notificationSettingFragment
    override val memberType: MemberType = MemberType.Kid("")
}