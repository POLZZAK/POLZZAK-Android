package com.polzzak_android.presentation.common.widget

import android.app.ActionBar.LayoutParams
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.polzzak_android.R
import com.polzzak_android.databinding.LayoutBackPressedSnackbarBinding

class BackButtonPressedSnackBar(view: View, private val message: String, duration: Int) {

    private val context = view.context
    private val snackBar = Snackbar.make(view, "", duration)

    init {
        setLayout()
    }

    private fun setLayout() {
        val inflater = LayoutInflater.from(context)
        val binding: LayoutBackPressedSnackbarBinding =
            DataBindingUtil.inflate(inflater, R.layout.layout_back_pressed_snackbar, null, false)
        val layout = (snackBar.view as? ViewGroup) ?: return
        with(layout) {
            removeAllViews()
            setPadding(0, 0, 0, 0)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            addView(binding.root)
            layoutParams = (layoutParams as? FrameLayout.LayoutParams)?.apply {
                width = LayoutParams.WRAP_CONTENT
                height = LayoutParams.WRAP_CONTENT
                gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
                val bottomMargin =
                    resources.getDimension(R.dimen.back_pressed_snack_bar_margin_bottom).toInt()
                setMargins(0, 0, 0, bottomMargin)
            }
        }
        snackBar.animationMode = Snackbar.ANIMATION_MODE_FADE
        binding.tvMessage.text = message
    }

    fun show() {
        snackBar.show()
    }

    fun dismiss() {
        snackBar.dismiss()
    }
}