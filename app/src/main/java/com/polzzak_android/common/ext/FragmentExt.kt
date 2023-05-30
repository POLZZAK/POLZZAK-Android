package com.polzzak_android.common.ext

import androidx.fragment.app.Fragment
import com.polzzak_android.common.FragmentOwner
import com.polzzak_android.common.SocialLoginManager

@Deprecated("네비게이션 컴포넌트 적용")
fun Fragment.finish() {
    (activity as? FragmentOwner)?.run {
        closeFragment()
    }
}

fun Fragment.openFragment(fragment: Fragment) {
    (activity as? FragmentOwner)?.run {
        openFragment(fragment)
    }
}

fun Fragment.getSocialLoginManager(): SocialLoginManager? = activity as? SocialLoginManager