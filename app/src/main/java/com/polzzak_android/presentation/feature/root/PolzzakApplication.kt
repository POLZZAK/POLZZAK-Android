package com.polzzak_android.presentation.feature.root

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.polzzak_android.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class PolzzakApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKakaoLogin()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initKakaoLogin() {
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }
}