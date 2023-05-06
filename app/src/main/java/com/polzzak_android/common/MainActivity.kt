package com.polzzak_android.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.polzzak_android.R
import com.polzzak_android.common.base.BaseActivity
import com.polzzak_android.databinding.ActivityMainBinding
import com.polzzak_android.presentation.login.LoginFragment
import com.polzzak_android.presentation.splash.SplashFragment
import com.polzzak_android.presentation.splash.SplashFragment.Companion.RESULT_TERMINATE_KEY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(), FragmentOwner {
    override val layoutResId: Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFragmentResultListener()
        openSplashFragment()
    }

    override fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(binding.fcvContainer.id, fragment)
            .addToBackStack(null).commit()
    }

    override fun closeFragment() {
        supportFragmentManager.popBackStack()
    }

    //TODO 로그아웃에서 사용할 수도 있음 + 작동하는지 테스트 필요
    override fun clearFragment() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    private fun initFragmentResultListener() {
        initSplashFragmentResultListener()
    }

    private fun initSplashFragmentResultListener() {
        supportFragmentManager.setFragmentResultListener(
            SPLASH_FRAGMENT_REQUEST_KEY,
            this
        ) { _, bundle ->
            closeFragment()
            val terminateValue = bundle.getBoolean(RESULT_TERMINATE_KEY, false)
            if (terminateValue) {
                openLoginFragment()
            }
        }
    }

    private fun initLoginFragmentResultListener(){
        supportFragmentManager.setFragmentResultListener(
            LOGIN_FRAGMENT_REQUEST_KEY,
            this
        ) { _, bundle ->
            closeFragment()

        }
    }
    private fun openSplashFragment() {
        val splashFragment = SplashFragment.newInstance(requestKey = SPLASH_FRAGMENT_REQUEST_KEY)
        openFragment(splashFragment)
    }

    private fun openLoginFragment() {
        val loginFragment = LoginFragment.newInstance(requestKey = LOGIN_FRAGMENT_REQUEST_KEY)
        openFragment(loginFragment)
    }

    companion object {
        private const val SPLASH_FRAGMENT_REQUEST_KEY = "splash_fragment_request_key"
        private const val LOGIN_FRAGMENT_REQUEST_KEY = "login_fragment_request_key"
    }
}