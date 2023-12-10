package com.polzzak_android.presentation.feature.stamp.model

import androidx.annotation.DrawableRes
import com.polzzak_android.R

enum class StampIcon(
    @DrawableRes val resId: Int,
    val title: String
) {
    NONE(0, ""),    // 인덱스 번호 밀기 위해 추가
    STAMP_1(R.drawable.ic_stamp_1, "Level Up"),
    STAMP_2(R.drawable.ic_stamp_2, "Good Job"),
    STAMP_3(R.drawable.ic_stamp_3, "짱 잘했어요"),
    STAMP_4(R.drawable.ic_stamp_4, "100점"),
    STAMP_5(R.drawable.ic_stamp_5, "힘 내!"),
    STAMP_6(R.drawable.ic_stamp_6, "사랑해"),
    STAMP_7(R.drawable.ic_stamp_7, "으라차차"),
    STAMP_8(R.drawable.ic_stamp_8, "쓰담쓰담"),
    STAMP_9(R.drawable.ic_stamp_9, "쓱쓱싹싹"),
}