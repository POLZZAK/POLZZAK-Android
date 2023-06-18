package com.polzzak_android.presentation.makingStamp

import android.widget.TextView

interface StampCountInteraction {
    fun onStampCountClicked(view: TextView, value: Int)
}