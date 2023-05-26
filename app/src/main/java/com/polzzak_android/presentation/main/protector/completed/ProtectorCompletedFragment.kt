package com.polzzak_android.presentation.main.protector.completed

import com.polzzak_android.R
import com.polzzak_android.common.base.BaseFragment
import com.polzzak_android.databinding.FragmentCompletedBinding

class ProtectorCompletedFragment : BaseFragment<FragmentCompletedBinding>() {
    override val layoutResId: Int = R.layout.fragment_completed

    companion object {
        private var instance: ProtectorCompletedFragment? = null

        @JvmStatic
        fun getInstance(): ProtectorCompletedFragment {
            if (instance == null) {
                synchronized(ProtectorCompletedFragment::class.java) {
                    if (instance == null) {
                        instance = ProtectorCompletedFragment()
                    }
                }
            }
            return instance!!
        }
    }
}