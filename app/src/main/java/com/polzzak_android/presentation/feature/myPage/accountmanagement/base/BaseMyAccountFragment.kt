package com.polzzak_android.presentation.feature.myPage.accountmanagement.base

import androidx.databinding.ViewDataBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.feature.root.host.RootNavigationOwner

abstract class BaseMyAccountFragment<B : ViewDataBinding> : BaseFragment<B>() {
    protected fun findRootNavigationOwner(): RootNavigationOwner? {
        var parentFragment = parentFragment
        while (parentFragment != null) {
            if (parentFragment is RootNavigationOwner) return parentFragment
            parentFragment = parentFragment.parentFragment
        }
        return null
    }
}