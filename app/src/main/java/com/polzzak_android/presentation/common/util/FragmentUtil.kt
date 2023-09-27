package com.polzzak_android.presentation.common.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.polzzak_android.presentation.feature.auth.login.sociallogin.SocialLoginManager
import com.polzzak_android.presentation.feature.root.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

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
    val folderName = "Polzzak"
    val timestamp = System.currentTimeMillis()
    val imageName = "polzzak${timestamp}.png"

    val contentResolver = requireContext().contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        put(MediaStore.Images.Media.DATE_ADDED, timestamp)
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {   // Android 10(API 29)
        contentValues.apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, imageName)
            put(MediaStore.Images.Media.DATE_TAKEN, timestamp)
            put(MediaStore.Images.Media.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/$folderName")
            put(MediaStore.Images.Media.IS_PENDING, true)
        }

        // 이미지를 write할 파일의 위치 uri 획득
        val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        if (imageUri != null) {
            try {
                // 파일에 write할 수 있는 outputStream 생성
                val outputStream = contentResolver.openOutputStream(imageUri)
                outputStream?.use {
                    // outputStream을 통해 bitmap을 write
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
    } else {
        val imageFileFolderPath = "${Environment.getExternalStorageDirectory()}/$folderName"
        val imageFileFolder = File(imageFileFolderPath)     // 이미지를 저장할 폴더

        // 사진을 저장할 폴더가 없으면 만들기
        if (imageFileFolder.exists().not()) {
            imageFileFolder.mkdirs()
        }

        val imageFile = File(imageFileFolder, imageName)    // 이미지 파일

        try {
            // 이미지 파일에 write할 수 있는 outputStream 생성
            val outputStream: OutputStream = FileOutputStream(imageFile)
            outputStream.use {
                // outputStream을 통해 bitmap을 write
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }

            contentValues.put(MediaStore.Images.Media.DATA, imageFile.absolutePath)
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            return@withContext Result.success(Unit)
        } catch (e: Throwable) {
            e.printStackTrace()
            return@withContext Result.failure(e)
        }
    }

    // 성공하면 여기까지 도달하지 않음
    return@withContext Result.failure(Exception("저장 실패"))
}

fun Fragment.logout() {
    (activity as? MainActivity)?.logout()
}

fun Fragment.handleInvalidToken() {
    (activity as? MainActivity)?.handleInvalidToken()
}