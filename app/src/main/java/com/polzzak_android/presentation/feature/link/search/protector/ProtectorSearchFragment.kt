package com.polzzak_android.presentation.feature.link.search.protector

import com.polzzak_android.R
import com.polzzak_android.presentation.feature.link.model.LinkMemberType
import com.polzzak_android.presentation.feature.link.search.base.BaseSearchFragment

class ProtectorSearchFragment : BaseSearchFragment() {
    override val linkMemberType = LinkMemberType.PROTECTOR
    override val targetLinkMemberType = LinkMemberType.KID
    override val actionMoveMainFragment: Int =
        R.id.action_protectorSearchFragment_to_protectorHostFragment
}