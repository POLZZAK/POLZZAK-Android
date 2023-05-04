package com.polzzak_android.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.polzzak_android.R
import com.polzzak_android.common.base.BaseActivity
import com.polzzak_android.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutResId: Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(binding.fcvContainer.id, fragment)
            .addToBackStack(null).commit()
    }

    fun closeFragment() {
        supportFragmentManager.popBackStack()
    }
}