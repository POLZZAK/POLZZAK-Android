package com.polzzak_android.presentation.feature.stamp.model

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
