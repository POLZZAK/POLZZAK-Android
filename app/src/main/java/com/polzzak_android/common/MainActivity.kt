package com.polzzak_android.common

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.polzzak_android.R
import com.polzzak_android.common.base.BaseActivity
import com.polzzak_android.common.sociallogin.GoogleLoginHelper
import com.polzzak_android.common.sociallogin.KakaoLoginHelper
import com.polzzak_android.databinding.ActivityMainBinding
import com.polzzak_android.presentation.login.LoginFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(), FragmentOwner, SocialLoginManager {
    override val layoutResId: Int = R.layout.activity_main
    private val mainViewModel by viewModels<MainViewModel>()

    private lateinit var navController: NavController

    private var googleLoginHelper: GoogleLoginHelper? = null
    private var kakaoLoginHelper: KakaoLoginHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        // set navigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.fcvContainer.id) as NavHostFragment
        navController = navHostFragment.navController

        // set bottom nav
        val btmNav = binding.btmNav
        btmNav.setupWithNavController(navController)

        initLoginFragmentResultListener()
        initLoginHelper()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    @Deprecated("네비게이션 컴포넌트 적용")
    override fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(binding.fcvContainer.id, fragment)
            .addToBackStack(null).commit()
    }

    @Deprecated("네비게이션 컴포넌트 적용")
    override fun closeFragment() {
        supportFragmentManager.popBackStack()
    }

    //TODO 로그아웃에서 사용할 수도 있음 + 작동하는지 테스트 필요
    override fun clearFragment() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    private fun initLoginHelper() {
        googleLoginHelper = GoogleLoginHelper(activity = this).apply {
            setLoginSuccessCallback {
                it.serverAuthCode?.let { authCode ->
                    mainViewModel.requestGoogleLogin(authCode = authCode)
                } ?: run {
                    //TODO id값, authcode가 안내려온 경우
                }
            }
        }
        kakaoLoginHelper = KakaoLoginHelper(context = this).apply {
            setLoginSuccessCallback { token ->
                mainViewModel.requestKakaoLogin(accessToken = token.accessToken)
            }
        }
    }

    private fun initLoginFragmentResultListener() {
        supportFragmentManager.setFragmentResultListener(
            LOGIN_FRAGMENT_REQUEST_KEY,
            this
        ) { _, bundle ->
            closeFragment()
            val isNeedSignUp = bundle.getBoolean(LoginFragment.RESULT_IS_NEED_SIGN_UP_KEY, false)
            if (isNeedSignUp) {
                //TODO 회원가입으로 이동
            } else {
                //TODO 메인페이지로 이동
            }
        }
    }

    private fun openLoginFragment() {
        val loginFragment = LoginFragment.newInstance(requestKey = LOGIN_FRAGMENT_REQUEST_KEY)
        openFragment(loginFragment)
    }

    override fun requestLoginGoogle() {
        googleLoginHelper?.requestLogin()
    }

    override fun requestLoginKakao() {
        kakaoLoginHelper?.requestLogin()
    }

    companion object {
        private const val LOGIN_FRAGMENT_REQUEST_KEY = "login_fragment_request_key"
    }

}