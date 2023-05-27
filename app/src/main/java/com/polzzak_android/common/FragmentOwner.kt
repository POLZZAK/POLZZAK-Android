package com.polzzak_android.common

import androidx.fragment.app.Fragment

@Deprecated("네비게이션 컴포넌트 적용")
interface FragmentOwner {
    fun openFragment(fragment: Fragment)
    fun closeFragment()
    fun clearFragment()
}