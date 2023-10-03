package com.polzzak_android.presentation.common.util

import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.polzzak_android.BuildConfig
import timber.log.Timber

class InAppUpdateChecker(private val activity: AppCompatActivity) {
    private val activityResultLauncher =
        activity.registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result: ActivityResult ->
            if (result.resultCode != AppCompatActivity.RESULT_OK) {
                Timber.e("인앱 업데이트 실패")
            }
        }

    private val appUpdateManager = AppUpdateManagerFactory.create(activity)

    fun checkUpdate(
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                // Request an immediate update.
                val versionCode = BuildConfig.VERSION_CODE
                val newestVersionCode = appUpdateInfo.availableVersionCode()
                if (newestVersionCode / 1000000 > versionCode / 1000000) {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        activityResultLauncher,
                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                    )
                } else {
                    onSuccess.invoke()
                }
            } else {
                onSuccess.invoke()
            }
        }
        appUpdateInfoTask.addOnFailureListener {
            onFailure.invoke()
        }
    }

    fun checkNewestVersion(
        onSuccess: (newestVersion: Int, version: Int) -> Unit,
        onFailure: () -> Unit
    ) {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            val newestVersionCode = appUpdateInfo.availableVersionCode()
            val versionCode = BuildConfig.VERSION_CODE
            onSuccess(newestVersionCode, versionCode)
        }
        appUpdateInfoTask.addOnFailureListener {
            onFailure.invoke()
        }
    }
}