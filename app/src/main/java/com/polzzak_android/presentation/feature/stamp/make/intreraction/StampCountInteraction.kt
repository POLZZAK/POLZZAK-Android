package com.polzzak_android.presentation.feature.stamp.make.intreraction

import android.widget.TextView

interface StampCountInteraction {
    fun onStampCountClicked(view: TextView, value: Int)
}