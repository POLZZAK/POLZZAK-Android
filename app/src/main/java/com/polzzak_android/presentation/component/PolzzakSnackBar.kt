package com.polzzak_android.presentation.component

import android.view.LayoutInflater
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.polzzak_android.R
import com.polzzak_android.data.remote.model.ApiException
import com.polzzak_android.databinding.LayoutSnackbarBinding
import com.polzzak_android.presentation.common.util.toPx

/**
 * 폴짝 앱 내에서 쓰이는 공통 Sanckbar.
 */
object PolzzakSnackBar {

    /**
     * 표시할 Snackbar의 타입
     */
    enum class Type {
        SUCCESS, WARNING
    }

    fun make(view: View, message: String, type: Type): Snackbar {
        return createSnackBar(view, message, type)
    }

    fun make(view: View, @StringRes resId: Int, type: Type): Snackbar {
        return createSnackBar(view, view.context.getString(resId), type)
    }

    private fun createSnackBar(view: View, message: String, type: Type): Snackbar {
        // 커스텀 레이아웃 가져오기
        val binding: LayoutSnackbarBinding = DataBindingUtil.inflate(
            LayoutInflater.from(view.context),
            R.layout.layout_snackbar,
            null,
            false
        )

        binding.tvMessage.text = message

        // 타입에 따라 아이콘과 background 색상 지정
        when (type) {
            Type.SUCCESS -> {
                binding.ivIcon.setImageDrawable(AppCompatResources.getDrawable(view.context, R.drawable.ic_success))
                binding.root.backgroundTintList = AppCompatResources.getColorStateList(view.context, R.color.primary_600)
            }
            Type.WARNING -> {
                binding.ivIcon.setImageDrawable(AppCompatResources.getDrawable(view.context, R.drawable.ic_warning))
                binding.root.backgroundTintList = AppCompatResources.getColorStateList(view.context, R.color.error_500)
            }
        }

        // snackbar에 커스텀 레이아웃 지정
        val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG)
        with(snackbar.view as Snackbar.SnackbarLayout) {
            removeAllViews()
            setPadding(16.toPx(view.context), 0, 16.toPx(view.context), 8.toPx(view.context))
            setBackgroundColor(ContextCompat.getColor(view.context, android.R.color.transparent))
            addView(binding.root, 0)
        }

        return snackbar
    }
}

/**
 * 에러에 맞는 Snackbar를 생성합니다.
 *
 * @param view
 * @param exception [ApiException]인 경우 API 호출 에러, 그 외에는 네트워크 에러로 구분.
 */
fun PolzzakSnackBar.errorOf(view: View, exception: Throwable): Snackbar {
    val msgId = if (exception is ApiException) {
        R.string.common_snackbar_api_error
    } else {
        R.string.common_snackbar_network_error
    }

    return PolzzakSnackBar.make(view, msgId, PolzzakSnackBar.Type.WARNING)
}