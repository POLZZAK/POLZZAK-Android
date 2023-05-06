package com.polzzak_android.presentation.login

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.polzzak_android.R
import com.polzzak_android.common.base.BaseFragment
import com.polzzak_android.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    override val layoutResId = R.layout.fragment_login

    private val loginViewModel by viewModels<LoginViewModel>()

    override fun initView() {
        super.initView()
        binding.tvBtnStartGoogle.setOnClickListener {
            loginGoogle()
        }
        binding.tvBtnStartKakao.setOnClickListener {
            loginKakao()
        }
    }

    private fun loginGoogle() {

    }

    private fun loginKakao() {

    }

    override fun initObserver() {
        super.initObserver()

    }

    companion object {
        private const val ARGUMENT_REQUEST_KEY = "argument_request_key"

        const val RESULT_IS_NEED_SIGN_UP_KEY = "result_terminate_key"
        fun newInstance(requestKey: String) = LoginFragment().apply {
            arguments = bundleOf(ARGUMENT_REQUEST_KEY to requestKey)
        }
    }
}