package com.polzzak_android.common.ext

import androidx.fragment.app.Fragment
import com.polzzak_android.common.FragmentOwner

fun Fragment.finish() {
    (activity as? FragmentOwner)?.run {
        closeFragment()
    }
}