package com.polzzak_android.presentation.common

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.polzzak_android.R
import com.polzzak_android.presentation.common.base.BaseActivity
import com.polzzak_android.presentation.auth.login.sociallogin.GoogleLoginHelper
import com.polzzak_android.presentation.auth.login.sociallogin.KakaoLoginHelper
import com.polzzak_android.databinding.ActivityMainBinding
import com.polzzak_android.presentation.auth.login.sociallogin.SocialLoginManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(), SocialLoginManager {
    override val layoutResId: Int = R.layout.activity_main
    private val mainViewModel by viewModels<MainViewModel>()

    private lateinit var navController: NavController

    private var _googleLoginHelper: GoogleLoginHelper? = null
    override val googleLoginHelper get() = _googleLoginHelper

    private var _kakaoLoginHelper: KakaoLoginHelper? = null
    override val kakaoLoginHelper get() = _kakaoLoginHelper
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

    private fun initLoginHelper() {
        _googleLoginHelper = GoogleLoginHelper(activity = this)
        _kakaoLoginHelper = KakaoLoginHelper(context = this)
    }

    fun getAccessToken() = mainViewModel.accessToken
}