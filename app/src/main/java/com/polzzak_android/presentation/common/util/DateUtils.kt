package com.polzzak_android.presentation.common.util

import com.polzzak_android.presentation.component.dialog.SelectedDateModel
import java.time.LocalDate

fun getCurrentDate(): SelectedDateModel {
    val currentDate = LocalDate.now()
    return SelectedDateModel(currentDate.year, currentDate.month.value, currentDate.dayOfMonth)
}