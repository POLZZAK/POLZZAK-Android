package com.polzzak_android.presentation.link.search.kid

import com.polzzak_android.R
import com.polzzak_android.presentation.link.search.base.BaseSearchOptionMenuFragment

class KidSearchOptionMenuFragment : BaseSearchOptionMenuFragment() {
    //TODO 임시 drawable -> 디자인가이드 적용 필요
    override val iconDrawableRes: Int = R.drawable.logo_first

    override val contentStringRes: Int = R.string.search_main_kid_content
    override val actionNavigateHostFragment: Int =
        R.id.action_kidSearchOptionMenuFragment_to_kidHostFragment
    override val actionNavigateSearchFragment: Int =
        R.id.action_kidSearchOptionMenuFragment_to_kidSearchFragment
    override val searchButtonTextRes: Int = R.string.common_search_kid
}