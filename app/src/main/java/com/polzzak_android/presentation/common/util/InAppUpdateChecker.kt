package com.polzzak_android.presentation.common.util

import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.polzzak_android.BuildConfig
import com.polzzak_android.R
import com.polzzak_android.presentation.common.model.ButtonCount
import com.polzzak_android.presentation.common.model.CommonButtonModel
import com.polzzak_android.presentation.component.dialog.CommonDialogContent
import com.polzzak_android.presentation.component.dialog.CommonDialogHelper
import com.polzzak_android.presentation.component.dialog.CommonDialogModel
import com.polzzak_android.presentation.component.dialog.DialogStyleType
import com.polzzak_android.presentation.component.dialog.OnButtonClickListener
import timber.log.Timber
import kotlin.system.exitProcess

class InAppUpdateChecker(private val activity: AppCompatActivity) {
    private val appUpdateManager = AppUpdateManagerFactory.create(activity)
    val activityResultLauncher =
        activity.registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result: ActivityResult ->
            if (result.resultCode != AppCompatActivity.RESULT_OK) {
                activity.finish()
            } else {
                val packageManager: PackageManager = activity.packageManager
                val intent = packageManager.getLaunchIntentForPackage(activity.packageName)
                val componentName = intent?.component
                val mainIntent = Intent.makeRestartActivityTask(componentName)
                activity.startActivity(mainIntent)
                exitProcess(0)
            }
        }

    fun checkUpdate(
        onComplete: () -> Unit,
        onNetworkError: () -> Unit,
    ) {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (!NetworkUtil.isNetworkConnected(context = activity)) {
                onNetworkError.invoke()
                return@addOnSuccessListener
            }
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                // Request an immediate update.
                val versionCode = BuildConfig.VERSION_CODE
                val newestVersionCode = appUpdateInfo.availableVersionCode()
                if (newestVersionCode / 1000000 > versionCode / 1000000) {
                    CommonDialogHelper.getInstance(
                        content = CommonDialogModel(
                            type = DialogStyleType.ALERT,
                            content = CommonDialogContent(title = SpannableBuilder.build(activity) {
                                span(
                                    text = activity.getString(R.string.common_need_to_update),
                                    style = R.style.subtitle_18_600,
                                    textColor = R.color.gray_700
                                )
                            }),
                            button = CommonButtonModel(buttonCount = ButtonCount.ONE)
                        ),
                        onConfirmListener = {
                            object : OnButtonClickListener {
                                override fun setBusinessLogic() {
                                    appUpdateManager.startUpdateFlowForResult(
                                        appUpdateInfo,
                                        activityResultLauncher,
                                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                                    )
                                }

                                override fun getReturnValue(value: Any) {
                                    //do nothing
                                }
                            }
                        },
                    ).apply { isCancelable = false }.show(activity.supportFragmentManager, null)
                } else {
                    onComplete.invoke()
                }
            } else {
                onComplete.invoke()
            }
        }
        appUpdateInfoTask.addOnFailureListener {
            Timber.e("InAppUpdate Failure Exception : $it")
            onComplete.invoke()
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