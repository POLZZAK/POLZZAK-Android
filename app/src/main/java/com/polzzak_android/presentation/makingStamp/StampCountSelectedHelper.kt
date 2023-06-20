package com.polzzak_android.presentation.makingStamp

import android.widget.TextView
import androidx.core.content.ContextCompat
import com.polzzak_android.R

class StampCountSelectedHelper {

    var stampCount = 0       // 선택한 도장 개수

    companion object {
        private var instance: StampCountSelectedHelper? = null

        fun getInstance(): StampCountSelectedHelper {
            if (instance == null) {
                instance = StampCountSelectedHelper()
            }
            return instance!!
        }
    }

    fun initSelectedCount(view: TextView, value: Int) {
        val isSelected = checkSelectedCount(value)
        setSelectedCount(view, isSelected)
    }

    fun onCountClicked(view: TextView, value: Int) {
        val isSelected = checkSelectedCount(value)
        updateSelectedCount(isSelected, value)
        setSelectedCount(view, !isSelected)
    }

    fun resetSelectedCount(view: TextView) {
        stampCount = 0
        setSelectedCount(view, false)
    }

    private fun checkSelectedCount(value: Int): Boolean {
        return value == stampCount
    }

    private fun updateSelectedCount(isSelected: Boolean, value: Int) {
        if (stampCount == value) {
            stampCount = 0
        }
        if (!isSelected) {
            stampCount = value
        }
    }

    private fun setSelectedCount(view: TextView, isSelected: Boolean) {
        val context = view.context

        val textColor = if (isSelected) {
            R.color.primary_600
        } else {
            R.color.gray_400
        }

        val backgroundColor = if (isSelected) {
            R.drawable.bg_blue_stroke_blue_bg_r8
        } else {
            R.drawable.bg_gray_stroke_white_bg_r8
        }

        view.isSelected = isSelected
        view.setTextColor(ContextCompat.getColor(context, textColor))
        view.setBackgroundResource(backgroundColor)
    }
}