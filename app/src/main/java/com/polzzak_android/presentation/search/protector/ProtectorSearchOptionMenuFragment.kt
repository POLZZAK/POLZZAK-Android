package com.polzzak_android.presentation.search.protector

import com.polzzak_android.R
import com.polzzak_android.presentation.search.base.BaseSearchOptionMenuFragment

class ProtectorSearchOptionMenuFragment : BaseSearchOptionMenuFragment() {
    //TODO 임시 drawable -> 디자인가이드 적용 필요
    override val iconDrawableRes: Int = R.drawable.logo_third

    //TODO string resource로 변경
    override val contentString: String = "칭찬 도장판을 만들려면\n아이와 연동이 필요해요"
    override val actionNavigateHostFragment: Int =
        R.id.action_protectorSearchOptionMenuFragment_to_protectorHostFragment
    override val actionNavigateSearchFragment: Int =
        R.id.action_protectorSearchOptionMenuFragment_to_protectorSearchFragment
    override val searchButtonText: String = "보호자 찾기"
}