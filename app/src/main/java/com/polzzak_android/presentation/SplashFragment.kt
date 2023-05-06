package com.polzzak_android.presentation

import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import com.polzzak_android.R
import com.polzzak_android.common.base.BaseFragment
import com.polzzak_android.common.ext.finish
import com.polzzak_android.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : BaseFragment<FragmentSplashBinding>() {
    override val layoutResId = R.layout.fragment_splash

    override fun initView() {
        super.initView()

        //fixme 일정 시간 지나면 finish? or 터치 시 finish?
        lifecycleScope.launch {
            delay(3000)
            terminate()
        }
    }

    private fun terminate() {
        arguments?.getString(ARGUMENT_REQUEST_KEY)?.let { requestKey ->
            val terminateKey = arguments?.getString(ARGUMENT_TERMINATE_KEY)
            val bundle = bundleOf().apply {
                putBoolean(terminateKey, true)
            }
            setFragmentResult(requestKey, bundle)
        }
        finish()
    }

    companion object {
        //arguments
        private const val ARGUMENT_REQUEST_KEY = "argument_request_key"
        private const val ARGUMENT_TERMINATE_KEY = "argument_terminate_key"

        fun newInstance(requestKey: String, resultTerminateKey: String) = SplashFragment().apply {
            arguments = bundleOf(
                ARGUMENT_REQUEST_KEY to requestKey,
                ARGUMENT_TERMINATE_KEY to resultTerminateKey
            )
        }
    }
}