package com.example.polzzak_android.common

import android.os.Bundle
import android.os.PersistableBundle
import com.example.polzzak_android.R
import com.example.polzzak_android.common.base.BaseActivity
import com.example.polzzak_android.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutResId: Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }
}