package com.polzzak_android.presentation.feature.auth.login

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.kakao.sdk.auth.model.OAuthToken
import com.polzzak_android.R
import com.polzzak_android.common.util.livedata.EventWrapperObserver
import com.polzzak_android.data.remote.model.ApiException
import com.polzzak_android.databinding.FragmentLoginBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.model.MemberType
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.util.getSocialLoginManager
import com.polzzak_android.presentation.common.util.shotBackPressed
import com.polzzak_android.presentation.component.PolzzakSnackBar
import com.polzzak_android.presentation.component.errorOf
import com.polzzak_android.presentation.feature.auth.login.model.LoginInfoModel
import com.polzzak_android.presentation.feature.auth.model.SocialLoginType
import com.polzzak_android.presentation.feature.auth.signup.SignUpFragment
import com.polzzak_android.presentation.feature.root.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    override val layoutResId = R.layout.fragment_login

    private val mainViewModel by viewModels<MainViewModel>(ownerProducer = { requireActivity() })
    private val loginViewModel by viewModels<LoginViewModel>()
    private val lastSocialLoginViewModel by viewModels<LastSocialLoginViewModel>()

    private val googleLoginSuccessCallback: (GoogleSignInAccount) -> Unit = { googleSignInAccount ->
        googleSignInAccount.serverAuthCode?.let { authCode ->
            loginViewModel.requestGoogleLogin(authCode = authCode)
        } ?: run {
            loginHelperFailedCallback.invoke()
        }
    }
    private val kakaoLoginSuccessCallback: (OAuthToken) -> Unit = { token ->
        loginViewModel.requestKakaoLogin(accessToken = token.accessToken)
    }

    private val loginFailedCallback: (Exception) -> Unit = {
        PolzzakSnackBar.errorOf(binding.root, it).show()
    }

    private val loginHelperFailedCallback: () -> Unit =
        { loginFailedCallback(ApiException.UnknownError()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this) {
            shotBackPressed()
        }
    }

    override fun initView() {
        super.initView()
        val googleLoginHelper = getSocialLoginManager()?.googleLoginHelper
        val kakaoLoginHelper = getSocialLoginManager()?.kakaoLoginHelper

        googleLoginHelper?.registerLoginSuccessCallback(callback = googleLoginSuccessCallback)
        googleLoginHelper?.registerLoginFailedCallback(callback = loginHelperFailedCallback)
        kakaoLoginHelper?.registerLoginSuccessCallback(callback = kakaoLoginSuccessCallback)
        kakaoLoginHelper?.registerLoginFailedCallback(callback = loginHelperFailedCallback)
        with(binding)
        {
            tvBtnStartGoogle.setOnClickListener {
                googleLoginHelper?.requestLogin()
            }
            tvBtnStartKakao.setOnClickListener {
                kakaoLoginHelper?.requestLogin()
            }
            tvContent.text = createContentSpannable()

        }
        lastSocialLoginViewModel.loadLastSocialLoginType()
    }

    private fun createContentSpannable(): Spannable {
        val content = getString(R.string.login_content)
        val polzzakStr = getString(R.string.common_polzzak)
        return SpannableStringBuilder(content).apply {
            val contentColor = ContextCompat.getColor(binding.root.context, R.color.gray_600)
            val polzzakColor = ContextCompat.getColor(binding.root.context, R.color.primary_600)
            val polzzakStartIdx = content.indexOf(polzzakStr, 0)
            setSpan(
                ForegroundColorSpan(contentColor),
                0,
                content.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                ForegroundColorSpan(polzzakColor),
                polzzakStartIdx,
                polzzakStartIdx + polzzakStr.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    override fun onDestroyView() {
        getSocialLoginManager()?.run {
            googleLoginHelper?.unregisterLoginSuccessCallback(callback = googleLoginSuccessCallback)
            googleLoginHelper?.unregisterLoginFailedCallback(callback = loginHelperFailedCallback)
            kakaoLoginHelper?.unregisterLoginSuccessCallback(callback = kakaoLoginSuccessCallback)
            kakaoLoginHelper?.unregisterLoginFailedCallback(callback = loginHelperFailedCallback)
        }
        super.onDestroyView()
    }

    override fun initObserver() {
        super.initObserver()
        loginViewModel.loginInfoLiveData.observe(viewLifecycleOwner, EventWrapperObserver {
            when (it) {
                is ModelState.Loading -> binding.clLoading.isVisible = true
                is ModelState.Success -> {
                    when (it.data) {
                        is LoginInfoModel.SignUp -> signUp(model = it.data)
                        is LoginInfoModel.Login -> login(model = it.data)
                    }
                    binding.clLoading.isVisible = false

                }

                is ModelState.Error -> {
                    loginFailedCallback.invoke(it.exception)
                    binding.clLoading.isVisible = false
                }
            }
        })
        lastSocialLoginViewModel.lastSocialLoginTypeLiveData.observe(viewLifecycleOwner) {
            with(binding) {
                tvLastLoginGoogle.isVisible = (it == SocialLoginType.GOOGLE)
                tvLastLoginKakao.isVisible = (it == SocialLoginType.KAKAO)
            }
        }
    }

    private fun signUp(model: LoginInfoModel.SignUp) {
        val signUpBundle = Bundle().apply {
            putString(SignUpFragment.ARGUMENT_USER_ID_KEY, model.userName)
            putParcelable(
                SignUpFragment.ARGUMENT_SOCIAL_LOGIN_TYPE_KEY,
                model.socialType
            )
            val parentTypes = ArrayList(model.parentTypes)
            putParcelableArrayList(
                SignUpFragment.ARGUMENT_PARENT_TYPES_KEY,
                parentTypes
            )
        }
        findNavController().navigate(
            R.id.action_loginFragment_to_SignUpFragment,
            signUpBundle
        )
    }

    private fun login(model: LoginInfoModel.Login) {
        mainViewModel.login(accessToken = model.accessToken)
        lastSocialLoginViewModel.saveLastSocialLoginType(model.socialType)
        val navAction = when (model.memberType) {
            is MemberType.Kid -> R.id.action_loginFragment_to_kidHostFragment
            is MemberType.Parent -> R.id.action_loginFragment_to_protectorHostFragment
        }
        findNavController().navigate(navAction)
    }
}