package com.polzzak_android.presentation.feature.link.search.protector

import com.polzzak_android.R
import com.polzzak_android.presentation.feature.link.search.base.BaseSearchOptionMenuFragment

class ProtectorSearchOptionMenuFragment : BaseSearchOptionMenuFragment() {
    override val iconDrawableRes: Int = R.drawable.ic_search_option_menu_parent

    override val contentStringRes: Int = R.string.search_main_parent_content
    override val actionNavigateHostFragment: Int =
        R.id.action_protectorSearchOptionMenuFragment_to_protectorHostFragment
    override val actionNavigateSearchFragment: Int =
        R.id.action_protectorSearchOptionMenuFragment_to_protectorSearchFragment
    override val searchButtonTextRes: Int = R.string.common_search_kid
}