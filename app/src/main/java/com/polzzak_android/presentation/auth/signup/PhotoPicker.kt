package com.polzzak_android.presentation.auth.signup

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class PhotoPicker : DefaultLifecycleObserver {
    private var activityResultCaller: ActivityResultCaller? = null
    private var shouldShowRequestPermissionRationale: ((String) -> Boolean)? = null
    private var context: Context? = null
    private var photoPickerResultCallback: ((uri: Uri) -> Unit)? = null
    private var requestPermissionLauncher: ActivityResultLauncher<String>? = null
    private var photoPickerLauncher: ActivityResultLauncher<Intent>? = null
    private val mediaReadPermission =
        if (VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) android.Manifest.permission.READ_MEDIA_IMAGES else android.Manifest.permission.READ_EXTERNAL_STORAGE

    constructor(activity: AppCompatActivity) {
        activityResultCaller = activity
        shouldShowRequestPermissionRationale = activity::shouldShowRequestPermissionRationale
        context = activity
        activity.lifecycle.addObserver(this)
        initLauncher()
    }

    constructor(fragment: Fragment) {
        activityResultCaller = fragment
        shouldShowRequestPermissionRationale = fragment::shouldShowRequestPermissionRationale
        context = fragment.context
        fragment.viewLifecycleOwner.lifecycle.addObserver(this)
        initLauncher()
    }

    private fun initLauncher() {
        requestPermissionLauncher =
            activityResultCaller?.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) launchPhotoPicker()
                else {
                    context?.let { Toast.makeText(it, "미디어 접근 권한 취소", Toast.LENGTH_LONG).show() }
                }
            }
        photoPickerLauncher = activityResultCaller?.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let {
                    photoPickerResultCallback?.invoke(it)
                }
            }
        }
    }

    private fun launchPhotoPicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        photoPickerLauncher?.launch(intent)
    }

    operator fun invoke(callback: (Uri) -> Unit) {
        photoPickerResultCallback = callback
        val isGranted = context?.let {
            ContextCompat.checkSelfPermission(
                it,
                mediaReadPermission
            ) == PackageManager.PERMISSION_GRANTED
        } ?: false
        if (isGranted) launchPhotoPicker()
        else requestPermissionLauncher?.launch(mediaReadPermission)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        photoPickerResultCallback = null
        requestPermissionLauncher = null
        photoPickerLauncher = null
        photoPickerResultCallback = null
        activityResultCaller = null
        shouldShowRequestPermissionRationale = null
        context = null
    }
}
