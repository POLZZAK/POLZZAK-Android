package com.polzzak_android.presentation.login

import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.polzzak_android.R
import com.polzzak_android.common.MainViewModel
import com.polzzak_android.common.base.BaseFragment
import com.polzzak_android.common.ext.finish
import com.polzzak_android.common.ext.getSocialLoginManager
import com.polzzak_android.common.model.ApiResult
import com.polzzak_android.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

//TODO google login release keystore 추가(현재 debug keystore만 사용 중)
@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    override val layoutResId = R.layout.fragment_login

    private val mainViewModel by viewModels<MainViewModel>(ownerProducer = { requireActivity() })

    override fun initView() {
        super.initView()
        binding.tvBtnStartGoogle.setOnClickListener {
            getSocialLoginManager()?.requestLoginGoogle()
        }
        binding.tvBtnStartKakao.setOnClickListener {
            getSocialLoginManager()?.requestLoginKakao()
        }

        // todo: 보호자 프래그먼트 이동 임시 나중에 삭제
        binding.tvHello.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_protectorHostFragment)
        }
    }

    override fun initObserver() {
        super.initObserver()
        mainViewModel.loginInfoLiveData.observe(viewLifecycleOwner) {
            //TODO api 반환값 처리
            when (it) {
                is ApiResult.Loading -> {
                    //TODO 로그인 인디케이터?
                }

                is ApiResult.Success -> {
                    setFragmentResult(isNeedSignUp = false)
                    finish()
                }

                is ApiResult.Error -> {
                    Timber.d("${it.data} ${it.code} ${it.statusCode}")
                    if (it.code == 412) {
                        setFragmentResult(isNeedSignUp = true)
                        finish()
                    } else {
                        //TODO 기타 에러처리
                    }
                }
            }
        }
    }

    private fun setFragmentResult(isNeedSignUp: Boolean) {
        val resultKey = arguments?.getString(ARGUMENT_REQUEST_KEY)
        resultKey?.let {
            setFragmentResult(it, bundleOf(RESULT_IS_NEED_SIGN_UP_KEY to isNeedSignUp))
        }
    }

    companion object {
        private const val ARGUMENT_REQUEST_KEY = "argument_request_key"

        const val RESULT_IS_NEED_SIGN_UP_KEY = "result_is_need_sign_up_key"
        fun newInstance(requestKey: String) = LoginFragment().apply {
            arguments = bundleOf(ARGUMENT_REQUEST_KEY to requestKey)
        }
    }
}