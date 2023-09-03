package com.polzzak_android.presentation.common.util

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.polzzak_android.presentation.component.dialog.DialogStyleType

object SetVisibility {
    @JvmStatic
    @BindingAdapter("setVisibility")
    fun setVisibility(view: View, isVisible: Boolean) {
        if (isVisible) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }

    @JvmStatic
    @BindingAdapter("setContainerVisibility")
    fun ConstraintLayout.setDialogContainerVisibility(type: DialogStyleType) {
        when (type) {
            DialogStyleType.ALERT, DialogStyleType.LOADING -> {
                this.visibility = View.GONE
            }

            DialogStyleType.MISSION, DialogStyleType.CALENDAR, DialogStyleType.STAMP -> {
                this.visibility = View.VISIBLE
            }
        }
    }
}