package com.polzzak_android.presentation.makingStamp.model

data class MakeStampRewardModel(
    val reward: String,
    val isValidate: Boolean,
    val errorMessage: String?
) {
    companion object {
        val init = MakeStampRewardModel(
            reward = "",
            isValidate = true,
            errorMessage = null
        )
    }
}
