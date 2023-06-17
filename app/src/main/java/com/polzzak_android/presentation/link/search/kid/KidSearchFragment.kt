package com.polzzak_android.presentation.link.search.kid

import com.polzzak_android.presentation.link.model.LinkMemberType
import com.polzzak_android.presentation.link.search.base.BaseSearchFragment

class KidSearchFragment : BaseSearchFragment() {
    override val linkMemberType = LinkMemberType.KID
    override val targetLinkMemberType = LinkMemberType.PROTECTOR
}