package com.polzzak_android.presentation.common.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.polzzak_android.presentation.feature.auth.login.sociallogin.SocialLoginManager
import com.polzzak_android.presentation.feature.root.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

fun Fragment.getSocialLoginManager(): SocialLoginManager? = activity as? SocialLoginManager

fun Fragment.getAccessTokenOrNull(): String? = (activity as? MainActivity)?.getAccessToken()

fun Fragment.shotBackPressed() {
    (activity as? MainActivity)?.backPressed()
}

fun Fragment.hideKeyboard() {
    activity?.run {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        currentFocus?.let { currentFocus ->
            inputManager?.hideSoftInputFromWindow(
                currentFocus.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}

fun Fragment.getPermissionManagerOrNull() = (activity as? MainActivity)?.permissionManager

suspend fun Fragment.saveBitmapToGallery(bitmap: Bitmap): Result<Unit> = withContext(Dispatchers.IO) {
    val timestamp = System.currentTimeMillis()

    val contentResolver = requireContext().contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        put(MediaStore.Images.Media.DATE_ADDED, timestamp)
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        contentValues.apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "polzzak${timestamp}.png")
            put(MediaStore.Images.Media.DATE_TAKEN, timestamp)
            put(MediaStore.Images.Media.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/Polzzak")
            put(MediaStore.Images.Media.IS_PENDING, true)
        }

        val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        if (imageUri != null) {
            try {
                val outputStream = contentResolver.openOutputStream(imageUri)
                outputStream?.use {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                }

                contentValues.put(MediaStore.Images.Media.IS_PENDING, false)
                contentResolver.update(imageUri, contentValues, null, null)

                return@withContext Result.success(Unit)
            } catch (e: Throwable) {
                e.printStackTrace()
                return@withContext Result.failure(e)
            }
        }
    }

    // TODO: 이하 버전에서도 저장할 수 있게 구현

    return@withContext Result.failure(Exception())
}