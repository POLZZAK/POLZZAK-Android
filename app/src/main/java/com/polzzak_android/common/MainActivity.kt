package com.polzzak_android.common

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.polzzak_android.R
import com.polzzak_android.common.base.BaseActivity
import com.polzzak_android.common.sociallogin.GoogleLoginHelper
import com.polzzak_android.common.sociallogin.KakaoLoginHelper
import com.polzzak_android.databinding.ActivityMainBinding
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
        initLoginHelper()

        // set navigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.fcvContainer.id) as NavHostFragment
        navController = navHostFragment.navController

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

    override fun requestLoginGoogle() {
        googleLoginHelper?.requestLogin()
    }

    override fun requestLoginKakao() {
        kakaoLoginHelper?.requestLogin()
    }
}