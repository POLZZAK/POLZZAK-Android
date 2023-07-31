package com.polzzak_android.presentation.feature.splash

import androidx.navigation.fragment.findNavController
import com.polzzak_android.R
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.databinding.FragmentSplashBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashFragment : BaseFragment<FragmentSplashBinding>() {
    override val layoutResId: Int = R.layout.fragment_splash
    private var timerJob: Job? = null

    override fun onResume() {
        super.onResume()
        if (timerJob?.isActive == true) return
        timerJob = CoroutineScope(Dispatchers.IO).launch {
            delay(SPLASH_DISPLAY_TIME)
            withContext(Dispatchers.Main) {
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        timerJob?.cancel()
        timerJob = null
    }

    companion object {
        private const val SPLASH_DISPLAY_TIME = 2000L
    }
}