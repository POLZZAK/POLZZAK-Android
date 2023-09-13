package com.polzzak_android.presentation.common.util

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.polzzak_android.R
import com.polzzak_android.presentation.common.model.ButtonCount
import com.polzzak_android.presentation.common.model.CommonButtonModel
import com.polzzak_android.presentation.component.dialog.CommonDialogContent
import com.polzzak_android.presentation.component.dialog.CommonDialogHelper
import com.polzzak_android.presentation.component.dialog.CommonDialogModel
import com.polzzak_android.presentation.component.dialog.DialogStyleType
import com.polzzak_android.presentation.component.dialog.OnButtonClickListener

class PermissionManager(private val activity: AppCompatActivity) {
    private val permissions =
        listOfNotNull(READ_MEDIA_PERMISSION, WRITE_MEDIA_PERMISSION, NOTIFICATION_PERMISSION)

    /**
     * 필요한 모든 권한에 대해 확인
     * 1회 거절할 경우 다시 묻지 않음
     * App 시작시만 실행
     */
    fun requestAllPermissions() {
        val needPermissions = permissions.filter { isFirstRequest(it) }.ifEmpty { return }
        ActivityCompat.requestPermissions(
            activity, needPermissions.toTypedArray(),
            MULTIPLE_PERMISSION_REQUEST_CODE
        )
    }

    private fun isFirstRequest(permission: String) =
        (ContextCompat.checkSelfPermission(
            activity,
            permission
        ) != PackageManager.PERMISSION_GRANTED) && !(ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            permission
        ))

    /**
     * 권한들이 허용되었는지 확인 후 허용되지 않았을 경우(false) 앱의 setting 화면으로 이동하여 권한 허용 유도
     * @param permissions 허용되었는지 확인할 권한들
     * @return true if all permission is granted else false
     */
    fun checkPermissionAndMoveSettingIfDenied(
        vararg permissions: String,
        dialogTitle: String
    ): Boolean {
        if (isPermissionsGranted(permissions = permissions)) return true
        val dialogTitleSpannable = SpannableBuilder.build(activity) {
            span(text = dialogTitle, textColor = R.color.gray_700, style = R.style.subtitle_18_600)
        }
        CommonDialogHelper.getInstance(
            content = CommonDialogModel(
                type = DialogStyleType.ALERT,
                content = CommonDialogContent(title = dialogTitleSpannable),
                button = CommonButtonModel(
                    buttonCount = ButtonCount.TWO,
                    negativeButtonText = activity.getString(R.string.permission_manager_dialog_btn_negative_text),
                    positiveButtonText = activity.getString(R.string.permission_manager_dialog_btn_positive_text)
                )
            ),
            onConfirmListener = {
                object : OnButtonClickListener {
                    override fun setBusinessLogic() {
                        val settingIntent =
                            Intent(ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:${activity.packageName}"))
                        activity.startActivity(settingIntent)
                    }

                    override fun getReturnValue(value: Any) {
                        //do nothing
                    }
                }
            }).show(activity.supportFragmentManager, null)
        return false
    }

    fun isPermissionsGranted(vararg permissions: String) =
        !permissions.any { permission ->
            ActivityCompat.checkSelfPermission(
                activity,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        }

    companion object {
        val READ_MEDIA_PERMISSION =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE
        const val WRITE_MEDIA_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val NOTIFICATION_PERMISSION =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.POST_NOTIFICATIONS else null
        const val MULTIPLE_PERMISSION_REQUEST_CODE = 91
    }
}