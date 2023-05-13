package com.polzzak_android.common

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import com.polzzak_android.BuildConfig
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
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.fcvContainer.id) as NavHostFragment
        navController = navHostFragment.navController

        // set bottom nav
        val btmNav = binding.btmNav
        btmNav.setupWithNavController(navController)

        initLoginFragmentResultListener()
        initGoogleLogin()
        initKakaoLogin()
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

    private fun initKakaoLogin() {
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
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
        //TODO 로그인 기능 정리하여 파일 이동
        with(UserApiClient.instance) {
//            logout{
//                me(callback = kakaoLoginSuccessCallback)
//                Log.d("MainActivity","logout : $it")
//            }

//            unlink{
//                me(callback = kakaoLoginSuccessCallback)
//                Log.d("MainActivity","unlink : $it")
//            }

            val kakaoLoginSuccessCallback: (User?, Throwable?) -> Unit = { user, error ->
                Log.d("MainActivity", "유저 id : ${user?.id}")
                //TODO 로그인 성공 콜백 구현
            }
            val kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.e("MainActivity", "카카오로 로그인 실패", error)
                } else if (token != null) {
                    Log.i("MainActivity", "카카오로 로그인 성공 ${token.accessToken}")
                    me(callback = kakaoLoginSuccessCallback)
                }
            }
            val kakaoTalkLoginCallback: (OAuthToken?, Throwable?) -> Unit =
                kakaoTalkLoginCallback@{ token, error ->
                    if (error != null) {
                        Log.e("MainActivity", "카카오톡 계정으로 로그인 실패", error)
                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) return@kakaoTalkLoginCallback
                        loginWithKakaoAccount(this@MainActivity, callback = kakaoLoginCallback)
                    } else if (token != null) {
                        Log.i(
                            "MainActivity",
                            "카카오톡 계정으로 로그인 성공 ${token.accessToken}"
                        )
                        me(callback = kakaoLoginSuccessCallback)
                    }
                }

            if (isKakaoTalkLoginAvailable(this@MainActivity)) {
                //카카오톡으로 로그인
                loginWithKakaoTalk(this@MainActivity, callback = kakaoTalkLoginCallback)
            } else {
                //카카오계정으로 로그인
                loginWithKakaoAccount(this@MainActivity, callback = kakaoLoginCallback)
            }
        }
    }

    companion object {
        private const val LOGIN_FRAGMENT_REQUEST_KEY = "login_fragment_request_key"
    }

}