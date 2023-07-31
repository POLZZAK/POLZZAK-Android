package com.polzzak_android.presentation.feature.stamp.model

data class MakeStampNameModel(
    val name: String,
    val isValidate: Boolean,
    val errorMessage: String?
) {
    companion object {
        val init = MakeStampNameModel(
            name = "",
            isValidate = true,
            errorMessage = null
        )
    }
}