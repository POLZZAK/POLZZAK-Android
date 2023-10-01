package com.polzzak_android.presentation.feature.link.search.kid

import com.polzzak_android.R
import com.polzzak_android.presentation.feature.link.search.base.BaseSearchOptionMenuFragment

class KidSearchOptionMenuFragment : BaseSearchOptionMenuFragment() {
    override val iconDrawableRes: Int = R.drawable.ic_search_option_menu_kid

    override val contentStringRes: Int = R.string.search_main_kid_content
    override val actionNavigateHostFragment: Int =
        R.id.action_kidSearchOptionMenuFragment_to_kidHostFragment
    override val actionNavigateSearchFragment: Int =
        R.id.action_kidSearchOptionMenuFragment_to_kidSearchFragment
    override val searchButtonTextRes: Int = R.string.common_search_parent
}