package com.polzzak_android.presentation.feature.root

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.polzzak_android.R
import com.polzzak_android.databinding.ActivityMainBinding
import com.polzzak_android.presentation.feature.auth.login.sociallogin.GoogleLoginHelper
import com.polzzak_android.presentation.feature.auth.login.sociallogin.KakaoLoginHelper
import com.polzzak_android.presentation.feature.auth.login.sociallogin.SocialLoginManager
import com.polzzak_android.presentation.common.base.BaseActivity
import com.polzzak_android.presentation.common.util.PermissionManager
import com.polzzak_android.presentation.component.BackButtonPressedSnackBar
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(), SocialLoginManager {
    override val layoutResId: Int = R.layout.activity_main
    private val mainViewModel by viewModels<MainViewModel>()

    private lateinit var navController: NavController

    private var _googleLoginHelper: GoogleLoginHelper? = null
    override val googleLoginHelper get() = _googleLoginHelper

    private var _kakaoLoginHelper: KakaoLoginHelper? = null
    override val kakaoLoginHelper get() = _kakaoLoginHelper

    private val compositeDisposable = CompositeDisposable()
    private val publishSubject = PublishSubject.create<Boolean>()

    private var backPressedSnackBar: BackButtonPressedSnackBar? = null

    val permissionManager = PermissionManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            val savedToken = it.getString(SAVE_INSTANCE_ACCESS_TOKEN_KEY)
            if (getAccessToken().isNullOrEmpty()) mainViewModel.accessToken = savedToken
        }
        permissionManager.requestAllPermissions()
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

    override fun onResume() {
        super.onResume()
        setBackPressedEvent()
    }

    override fun onPause() {
        super.onPause()
        clearBackPressedEvent()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVE_INSTANCE_ACCESS_TOKEN_KEY, getAccessToken())
    }

    private fun clearBackPressedEvent() {
        compositeDisposable.clear()
    }

    private fun setBackPressedEvent() {
        /**
         * 3초 이내 백버튼 2회 누를 경우 앱 종료
         * 참고 https://stackoverflow.com/a/44196568
         */
        compositeDisposable.add(publishSubject.debounce(100, TimeUnit.MICROSECONDS)
            .observeOn(AndroidSchedulers.mainThread()).doOnNext {
                val message = getString(R.string.common_snackbar_back_pressed_message)
                backPressedSnackBar = BackButtonPressedSnackBar(
                    binding.root,
                    message = message,
                    duration = BACK_BTN_DEBOUNCE_TIMER
                )
                backPressedSnackBar?.show()
            }.timeInterval(TimeUnit.MILLISECONDS).skip(1).filter {
                it.time() < BACK_BTN_DEBOUNCE_TIMER
            }.subscribe {
                finish()
            }
        )
    }

    fun backPressed() {
        publishSubject.onNext(true)
    }

    fun resetBackPressedEvent() {
        backPressedSnackBar?.dismiss()
        backPressedSnackBar = null
        clearBackPressedEvent()
        setBackPressedEvent()
    }

    companion object {
        private const val BACK_BTN_DEBOUNCE_TIMER = 3000
        private const val SAVE_INSTANCE_ACCESS_TOKEN_KEY = "save_instance_access_token_key"
    }
}