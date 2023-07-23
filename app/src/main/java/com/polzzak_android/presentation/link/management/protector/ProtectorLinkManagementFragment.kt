package com.polzzak_android.presentation.link.management.protector

import com.polzzak_android.presentation.link.management.base.BaseLinkManagementFragment
import com.polzzak_android.presentation.link.model.LinkMemberType

class ProtectorLinkManagementFragment : BaseLinkManagementFragment() {
    override val targetLinkMemberType: LinkMemberType = LinkMemberType.KID
    override val linkMemberType: LinkMemberType = LinkMemberType.PROTECTOR
}