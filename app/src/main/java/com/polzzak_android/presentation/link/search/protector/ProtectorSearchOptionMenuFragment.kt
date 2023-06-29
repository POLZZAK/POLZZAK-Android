package com.polzzak_android.presentation.link.search.protector

import com.polzzak_android.R
import com.polzzak_android.presentation.link.search.base.BaseSearchOptionMenuFragment

class ProtectorSearchOptionMenuFragment : BaseSearchOptionMenuFragment() {
    //TODO 임시 drawable -> 디자인가이드 적용 필요
    override val iconDrawableRes: Int = R.drawable.logo_third

    override val contentStringRes: Int = R.string.search_main_parent_content
    override val actionNavigateHostFragment: Int =
        R.id.action_protectorSearchOptionMenuFragment_to_protectorHostFragment
    override val actionNavigateSearchFragment: Int =
        R.id.action_protectorSearchOptionMenuFragment_to_protectorSearchFragment
    override val searchButtonTextRes: Int = R.string.common_search_parent
}