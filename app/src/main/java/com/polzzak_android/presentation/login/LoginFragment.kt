package com.polzzak_android.presentation.login

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.polzzak_android.R
import com.polzzak_android.common.MainViewModel
import com.polzzak_android.common.base.BaseFragment
import com.polzzak_android.common.ext.getSocialLoginManager
import com.polzzak_android.common.model.ApiResult
import com.polzzak_android.common.util.livedata.EventWrapperObserver
import com.polzzak_android.databinding.FragmentLoginBinding
import com.polzzak_android.presentation.signup.SignUpFragment
import dagger.hilt.android.AndroidEntryPoint

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
        mainViewModel.loginInfoLiveData.observe(viewLifecycleOwner, EventWrapperObserver {
            //TODO api 반환값 처리
            when (it) {
                is ApiResult.Loading -> {
                    //TODO 로그인 인디케이터?
                }

                is ApiResult.Success -> {
                    //TODO 로그인 성공 후 화면이동
                }

                is ApiResult.Error -> {
                    if (it.code == 412) {
                        val signUpBundle = Bundle().apply {
                            putString(SignUpFragment.ARGUMENT_USER_ID_KEY, it.data?.userName)
                            putSerializable(
                                SignUpFragment.ARGUMENT_SOCIAL_LOGIN_TYPE_KEY,
                                it.data?.userType
                            )
                        }
                        findNavController().navigate(
                            R.id.action_loginFragment_to_SignUpFragment,
                            signUpBundle
                        )
                    } else {
                        //TODO 기타 에러처리
                    }
                }
            }
        })
    }
}