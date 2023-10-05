package com.polzzak_android.presentation.feature.notification.list.protector

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.polzzak_android.R
import com.polzzak_android.presentation.common.model.MemberType
import com.polzzak_android.presentation.feature.notification.list.base.BaseNotificationListFragment
import com.polzzak_android.presentation.feature.notification.list.model.NotificationLinkType

class ProtectorNotificationListFragment : BaseNotificationListFragment() {
    override val actionToSettingFragment: Int =
        R.id.action_protectorNotificationFragment_to_notificationSettingFragment
    override val memberType: MemberType = MemberType.Parent("")
    override fun onClickPageLink(type: NotificationLinkType) {
        findNavController().run {
            val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.protectorBtmNav)
            when (type) {
                is NotificationLinkType.My -> bottomNav?.selectedItemId =
                    R.id.protectorMyPageFragment

                is NotificationLinkType.Home -> bottomNav?.selectedItemId =
                    R.id.protectorMainFragment

                is NotificationLinkType.CouponDetail -> {
                    val bundle = Bundle().apply { putInt("couponId", type.id) }
                    findNavController().navigate(R.id.action_to_couponDetailFragment, bundle)
                }

                is NotificationLinkType.StampDetail -> {
                    val bundle = Bundle().apply { putInt("boardId", type.id) }
                    findNavController().navigate(
                        R.id.action_protectorNotificationFragment_to_protectorStampBoardDetailFragment,
                        bundle
                    )

                }
            }
        }
    }
}