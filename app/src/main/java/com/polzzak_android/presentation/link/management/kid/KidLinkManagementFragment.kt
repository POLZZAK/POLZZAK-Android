package com.polzzak_android.presentation.link.management.kid

import com.polzzak_android.presentation.link.management.base.BaseLinkManagementFragment
import com.polzzak_android.presentation.link.model.LinkMemberType

class KidLinkManagementFragment : BaseLinkManagementFragment() {
    override val targetLinkMemberType: LinkMemberType = LinkMemberType.PROTECTOR
    override val linkMemberType: LinkMemberType = LinkMemberType.KID
}