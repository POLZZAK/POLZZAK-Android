package com.polzzak_android.presentation.search.kid

import com.polzzak_android.R
import com.polzzak_android.presentation.search.base.BaseSearchOptionMenuFragment

class KidSearchOptionMenuFragment : BaseSearchOptionMenuFragment() {
    //TODO 임시 drawable -> 디자인가이드 적용 필요
    override val iconDrawableRes: Int = R.drawable.logo_first

    //TODO string resource로 변경
    override val contentString: String = "칭찬 도장판을 받으려면\n보호자와 연동이 필요해요"
    override val actionNavigateHostFragment: Int =
        R.id.action_kidSearchOptionMenuFragment_to_kidHostFragment
    override val actionNavigateSearchDialogFragment: Int =
        R.id.action_kidSearchOptionMenuFragment_to_kidSearchDialogFragment
    override val searchButtonText: String = "아이 찾기"
}