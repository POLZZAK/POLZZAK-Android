package com.polzzak_android.presentation.login

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.polzzak_android.R
import com.polzzak_android.common.MainViewModel
import com.polzzak_android.common.base.BaseFragment
import com.polzzak_android.common.ext.getSocialLoginManager
import com.polzzak_android.common.liveData.SingleEventObserver
import com.polzzak_android.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

//TODO google login release keystore 추가(현재 debug keystore만 사용 중)
@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    override val layoutResId = R.layout.fragment_login

    private val mainViewModel by viewModels<MainViewModel>(ownerProducer = { requireActivity() })
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun initView() {
        super.initView()
        binding.tvBtnStartGoogle.setOnClickListener {
            getSocialLoginManager()?.requestLoginGoogle()
        }
        binding.tvBtnStartKakao.setOnClickListener {
            getSocialLoginManager()?.requestLoginKakao()
        }
    }

    override fun initObserver() {
        super.initObserver()
        mainViewModel.socialLoginResult.observe(viewLifecycleOwner, SingleEventObserver {
        })
    }

    companion object {
        private const val ARGUMENT_REQUEST_KEY = "argument_request_key"

        const val RESULT_IS_NEED_SIGN_UP_KEY = "result_terminate_key"
        fun newInstance(requestKey: String) = LoginFragment().apply {
            arguments = bundleOf(ARGUMENT_REQUEST_KEY to requestKey)
        }
    }
}