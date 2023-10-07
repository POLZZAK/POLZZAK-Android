package com.polzzak_android.presentation.feature.splash

import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentSplashBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.util.getInAppUpdateCheckerOrNull
import com.polzzak_android.presentation.component.PolzzakSnackBar
import com.polzzak_android.presentation.component.errorOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashFragment : BaseFragment<FragmentSplashBinding>() {
    override val layoutResId: Int = R.layout.fragment_splash
    private var timerJob: Job? = null
    private var isConnected = true

    private val networkCallback = object : NetworkCallback() {
        override fun onAvailable(network: Network) {
            if (isConnected) return
            isConnected = true
            getInAppUpdateCheckerOrNull()?.checkUpdate(
                onSuccess = ::startSplash,
                onNetworkError = ::onCheckInAppUpdateNetworkError
            )
        }

        override fun onLost(network: Network) {
            isConnected = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getInAppUpdateCheckerOrNull()?.checkUpdate(
            onSuccess = ::startSplash,
            onNetworkError = ::onCheckInAppUpdateNetworkError
        )
        registerNetworkCallback(networkCallback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unregisterNetworkCallback(networkCallback)
    }

    private fun registerNetworkCallback(networkCallback: NetworkCallback) {
        val connectivityManager =
            getSystemService(requireContext(), ConnectivityManager::class.java)
        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
        connectivityManager?.registerNetworkCallback(networkRequest, networkCallback)
    }

    private fun unregisterNetworkCallback(networkCallback: NetworkCallback) {
        val connectivityManager =
            getSystemService(requireContext(), ConnectivityManager::class.java)
        connectivityManager?.unregisterNetworkCallback(networkCallback)
    }

    private fun startSplash() {
        if (timerJob?.isActive == true) return
        timerJob = lifecycleScope.launch {
            delay(SPLASH_DISPLAY_TIME)
            withContext(Dispatchers.Main) {
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }
        }
    }

    private fun onCheckInAppUpdateNetworkError() {
        isConnected = false
        PolzzakSnackBar.errorOf(binding.root, Throwable()).show()
    }

    override fun onResume() {
        super.onResume()
        if (timerJob != null) startSplash()
    }


    override fun onPause() {
        super.onPause()
        timerJob?.cancel()
    }

    companion object {
        private const val SPLASH_DISPLAY_TIME = 2000L
    }
}