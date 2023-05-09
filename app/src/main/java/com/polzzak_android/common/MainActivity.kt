package com.polzzak_android.common

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.polzzak_android.R
import com.polzzak_android.common.base.BaseActivity
import com.polzzak_android.common.model.SocialLoginType
import com.polzzak_android.databinding.ActivityMainBinding
import com.polzzak_android.presentation.login.LoginFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(), FragmentOwner, SocialLoginManager {
    override val layoutResId: Int = R.layout.activity_main
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var resultLauncher: ActivityResultLauncher<Intent>? = null

    private val mainViewModel by viewModels<MainViewModel>()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        // set navigation
        val navHostFragment = supportFragmentManager.findFragmentById(binding.fcvContainer.id) as NavHostFragment
        navController = navHostFragment.navController

        // set bottom nav
        val btmNav = binding.btmNav
        btmNav.setupWithNavController(navController)

        initLoginFragmentResultListener()
        initGoogleLogin()
        openLoginFragment()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
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

    private fun initGoogleLogin() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    val account = task.getResult(ApiException::class.java)
                    val id = account?.id
                    id?.let {
                        mainViewModel.requestLogin(
                            id = it,
                            loginType = SocialLoginType.GOOGLE
                        )
                    }
                }
            }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
            .requestEmail().requestId().build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
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
        val signInIntent = mGoogleSignInClient?.signInIntent
        val lastAccount = GoogleSignIn.getLastSignedInAccount(this)
        lastAccount?.let {
            val id = it.id ?: return@let null
            mainViewModel.requestLogin(id = id, loginType = SocialLoginType.GOOGLE)
        } ?: run {
            resultLauncher?.launch(signInIntent)
        }
    }

    override fun requestLoginKakao() {
        //TODO 카카오 로그인 구현
    }

    companion object {
        private const val LOGIN_FRAGMENT_REQUEST_KEY = "login_fragment_request_key"
    }

}