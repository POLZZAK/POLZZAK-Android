package com.polzzak_android.presentation.link.search.protector

import com.polzzak_android.presentation.link.model.LinkMemberType
import com.polzzak_android.presentation.link.search.base.BaseSearchFragment

class ProtectorSearchFragment : BaseSearchFragment() {
    override val linkMemberType = LinkMemberType.PROTECTOR
    override val targetLinkMemberType = LinkMemberType.KID
}