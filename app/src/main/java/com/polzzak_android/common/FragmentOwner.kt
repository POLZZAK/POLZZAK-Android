package com.polzzak_android.common

import androidx.fragment.app.Fragment

interface FragmentOwner {
    fun openFragment(fragment: Fragment)
    fun closeFragment()
    fun clearFragment()
}