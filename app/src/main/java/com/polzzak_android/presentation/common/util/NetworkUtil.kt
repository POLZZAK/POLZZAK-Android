package com.polzzak_android.presentation.common.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object NetworkUtil {
    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
        val network = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return when {
            network == null -> false
            network.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            network.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            else -> false
        }
    }
}