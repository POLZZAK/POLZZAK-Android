package com.polzzak_android.presentation.feature.stamp.detail.protector.stampBottomSheet

data class CompleteStampModel(
        val id: Int,
        val name: String
)

fun getCompleteStampList(): List<CompleteStampModel> {
    return listOf(
            CompleteStampModel(1, "Level Up"),
            CompleteStampModel(2, "Good Job"),
            CompleteStampModel(3, "짱 잘했어요"),
            CompleteStampModel(4, "100점"),
            CompleteStampModel(5, "힘 내!"),
            CompleteStampModel(6, "사랑해"),
            CompleteStampModel(7, "으라차차"),
            CompleteStampModel(8, "쓰담쓰담"),
            CompleteStampModel(9, "쓱쓱싹싹")
    )
}
