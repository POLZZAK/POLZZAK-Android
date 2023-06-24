package com.polzzak_android.presentation.makingStamp.model

data class MakeStampCountModel(
    val count: Int,
    val isValidate: Boolean,
    val errorMessage: String?
) {
    companion object {
        val init = MakeStampCountModel(
            count = 0,
            isValidate = true,
            errorMessage = null
        )
    }
}