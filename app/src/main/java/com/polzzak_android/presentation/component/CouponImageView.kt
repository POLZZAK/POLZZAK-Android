package com.polzzak_android.presentation.component

import android.content.Context
import android.util.AttributeSet
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.unit.dp
import com.polzzak_android.presentation.feature.coupon.detail.CouponTicketImage
import com.polzzak_android.presentation.feature.coupon.model.CouponDetailModel

/**
 * 쿠폰 이미지 컴포저블을 View로 만든 클래스.
 * 화면에 표시한 쿠폰 컴포저블을 View로 얻어 Bitmap으로 바꾸기 위해 사용합니다.
 */
class CouponImageView @JvmOverloads constructor(
    private val data: CouponDetailModel,
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbstractComposeView(context, attrs, defStyleAttr) {

    var onMissionClick: (() -> Unit)? = null

    @Composable
    override fun Content() {
        CouponTicketImage(
            data = data,
            onMissionClick = onMissionClick,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
    }
}