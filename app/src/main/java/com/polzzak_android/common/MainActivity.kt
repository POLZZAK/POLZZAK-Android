package com.polzzak_android.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.polzzak_android.R
import com.polzzak_android.common.base.BaseActivity
import com.polzzak_android.databinding.ActivityMainBinding
import com.polzzak_android.presentation.SplashFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(), FragmentOwner {
    override val layoutResId: Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFragmentResultListener()
        val splashFragment = SplashFragment.newInstance(
            requestKey = SPLASH_FRAGMENT_REQUEST_KEY,
            resultTerminateKey = SPLASH_FRAGMENT_RESULT_TERMINATE_KEY
        )
        openFragment(splashFragment)
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

        }
    }

    companion object {
        //SplashFragment ResultListener
        private const val SPLASH_FRAGMENT_REQUEST_KEY = "splash_fragment_request_key"
        private const val SPLASH_FRAGMENT_RESULT_TERMINATE_KEY =
            "splash_fragment_result_terminate_key"
    }
}