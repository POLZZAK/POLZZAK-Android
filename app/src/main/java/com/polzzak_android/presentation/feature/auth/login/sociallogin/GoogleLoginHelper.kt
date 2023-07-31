package com.polzzak_android.presentation.feature.auth.login.sociallogin

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.polzzak_android.BuildConfig
import timber.log.Timber

//TODO 로그아웃 콜백 추가
class GoogleLoginHelper(activity: AppCompatActivity) {
    private var loginSuccessCallbacks: MutableList<(GoogleSignInAccount) -> Unit> =
        mutableListOf()

    private val mGoogleSignInClient: GoogleSignInClient
    private val resultLauncher: ActivityResultLauncher<Intent> =
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                val account = task.getResult(ApiException::class.java)
                loginSuccessCallbacks.forEach {
                    it.invoke(account)
                }
            } else {
                //TODO 구글 로그인 요청 실패 에러 핸들링
                Timber.e("구글 로그인 실패 ${result.resultCode}")
            }
        }

    init {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(BuildConfig.GOOGLE_WEB_APPLICATION_CLIENT_ID).requestId()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso)
    }

    fun registerLoginSuccessCallback(callback: (GoogleSignInAccount) -> Unit) {
        loginSuccessCallbacks.add(callback)
    }

    fun unregisterLoginSuccessCallback(callback: (GoogleSignInAccount) -> Unit): Boolean {
        return loginSuccessCallbacks.removeIf { it == callback }
    }

    fun requestLogin() {
        val signInIntent = mGoogleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }

    fun requestLogout() {
        mGoogleSignInClient.signOut()
    }
}