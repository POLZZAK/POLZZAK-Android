package com.polzzak_android.presentation.feature.auth.signup

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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
        launchPhotoPicker()
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
