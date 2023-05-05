package com.polzzak_android.temp.hiltsample

import javax.inject.Inject


class HiltSampleRepository @Inject constructor() {
    private var count = 0
    fun fetchSampleCount() = ++count
}