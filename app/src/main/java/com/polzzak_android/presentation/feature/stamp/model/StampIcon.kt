package com.polzzak_android.presentation.feature.stamp.model

import androidx.annotation.DrawableRes
import com.polzzak_android.R

enum class StampIcon(@DrawableRes val resId: Int) {
    NONE(0),    // 인덱스 번호 밀기 위해 추가
    STAMP_1(R.drawable.ic_stamp_1),
    STAMP_2(R.drawable.ic_stamp_2),
    STAMP_3(R.drawable.ic_stamp_3),
    STAMP_4(R.drawable.ic_stamp_4),
    STAMP_5(R.drawable.ic_stamp_5),
    STAMP_6(R.drawable.ic_stamp_6),
    STAMP_7(R.drawable.ic_stamp_7),
    STAMP_8(R.drawable.ic_stamp_8),
    STAMP_9(R.drawable.ic_stamp_9),
}