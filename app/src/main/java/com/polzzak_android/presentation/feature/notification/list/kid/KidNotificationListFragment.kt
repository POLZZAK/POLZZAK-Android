package com.polzzak_android.presentation.feature.notification.list.kid

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.polzzak_android.R
import com.polzzak_android.presentation.common.model.MemberType
import com.polzzak_android.presentation.feature.notification.list.base.BaseNotificationListFragment
import com.polzzak_android.presentation.feature.notification.list.model.NotificationLinkType
import timber.log.Timber

class KidNotificationListFragment : BaseNotificationListFragment() {
    override val actionToSettingFragment: Int =
        R.id.action_kidNotificationFragment_to_kidNotificationSettingFragment
    override val memberType: MemberType = MemberType.Kid("")
    override fun onClickPageLink(type: NotificationLinkType) {
        findNavController().run {
            val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.kidBtmNav)
            when (type) {
                is NotificationLinkType.My -> bottomNav?.selectedItemId = R.id.kid_myPage_nav_graph
                is NotificationLinkType.Home -> bottomNav?.selectedItemId = R.id.kid_main_nav_graph
                is NotificationLinkType.CouponDetail -> {
                    //TODO bundle, action
                    Timber.d("move to coupon detail ${type.id}")
                }

                is NotificationLinkType.StampDetail -> {
                    val bundle = Bundle().apply { putInt("boardId", type.id) }
                    findNavController().navigate(
                        R.id.action_kidNotificationFragment_to_kidStampBoardDetailFragment,
                        bundle
                    )
                }
            }
        }
    }

}