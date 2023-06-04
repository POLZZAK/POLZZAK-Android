package com.polzzak_android.presentation.auth.login

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.kakao.sdk.auth.model.OAuthToken
import com.polzzak_android.R
import com.polzzak_android.common.util.livedata.EventWrapperObserver
import com.polzzak_android.databinding.FragmentLoginBinding
import com.polzzak_android.presentation.auth.signup.SignUpFragment
import com.polzzak_android.presentation.common.MainViewModel
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.model.ApiResult
import com.polzzak_android.presentation.common.util.getSocialLoginManager
import dagger.hilt.android.AndroidEntryPoint

//TODO google login release keystore 추가(현재 debug keystore만 사용 중)
@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    override val layoutResId = R.layout.fragment_login

    private val mainViewModel by viewModels<MainViewModel>(ownerProducer = { requireActivity() })
    private val loginViewModel by viewModels<LoginViewModel>()

    private val googleLoginSuccessCallback: (GoogleSignInAccount) -> Unit = { googleSignInAccount ->
        googleSignInAccount.serverAuthCode?.let { authCode ->
            loginViewModel.requestGoogleLogin(authCode = authCode)
        } ?: run {
            //TODO id값, authcode가 안내려온 경우
        }
    }
    private val kakaoLoginSuccessCallback: (OAuthToken) -> Unit = { token ->
        loginViewModel.requestKakaoLogin(accessToken = token.accessToken)
    }

    override fun initView() {
        super.initView()
        val googleLoginHelper = getSocialLoginManager()?.googleLoginHelper
        val kakaoLoginHelper = getSocialLoginManager()?.kakaoLoginHelper

        googleLoginHelper?.registerLoginSuccessCallback(callback = googleLoginSuccessCallback)
        kakaoLoginHelper?.registerLoginSuccessCallback(callback = kakaoLoginSuccessCallback)
        binding.tvBtnStartGoogle.setOnClickListener {
            googleLoginHelper?.requestLogin()
        }
        binding.tvBtnStartKakao.setOnClickListener {
            kakaoLoginHelper?.requestLogin()
        }

        // todo: 보호자 프래그먼트 이동 임시 나중에 삭제
        binding.tvHello.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_protectorHostFragment)
        }
    }

    override fun onDestroyView() {
        getSocialLoginManager()?.run {
            googleLoginHelper?.unregisterLoginSuccessCallback(callback = googleLoginSuccessCallback)
            kakaoLoginHelper?.unregisterLoginSuccessCallback(callback = kakaoLoginSuccessCallback)
        }
        super.onDestroyView()
    }

    override fun initObserver() {
        super.initObserver()
        loginViewModel.loginInfoLiveData.observe(viewLifecycleOwner, EventWrapperObserver {
            //TODO api 반환값 처리
            when (it) {
                is ApiResult.Loading -> {
                    //TODO 로그인 인디케이터?
                }

                is ApiResult.Success -> {
                    if (it.data?.accessToken == null) {
                        val signUpBundle = Bundle().apply {
                            putString(SignUpFragment.ARGUMENT_USER_ID_KEY, it.data?.userName)
                            putParcelable(
                                SignUpFragment.ARGUMENT_SOCIAL_LOGIN_TYPE_KEY,
                                it.data?.socialType
                            )
                            val parentTypes = ArrayList(it.data?.parentTypes ?: emptyList())
                            putParcelableArrayList(
                                SignUpFragment.ARGUMENT_PARENT_TYPES_KEY,
                                parentTypes
                            )
                        }
                        findNavController().navigate(
                            R.id.action_loginFragment_to_SignUpFragment,
                            signUpBundle
                        )
                    } else {
                        //TODO 로그인 성공
                    }
                }

                is ApiResult.Error -> {
                    //TODO 에러처리
                }
            }
        })
    }
}