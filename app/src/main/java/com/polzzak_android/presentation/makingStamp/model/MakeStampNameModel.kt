package com.polzzak_android.presentation.makingStamp.model

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