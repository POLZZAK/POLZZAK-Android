package com.polzzak_android.presentation.splash

import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import com.polzzak_android.R
import com.polzzak_android.common.base.BaseFragment
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
            val bundle = bundleOf().apply {
                putBoolean(RESULT_TERMINATE_KEY, true)
            }
            setFragmentResult(requestKey, bundle)
        }
    }

    companion object {
        private const val ARGUMENT_REQUEST_KEY = "argument_request_key"

        const val RESULT_TERMINATE_KEY = "result_terminate_key"
        fun newInstance(requestKey: String) = SplashFragment().apply {
            arguments = bundleOf(
                ARGUMENT_REQUEST_KEY to requestKey,
            )
        }
    }
}