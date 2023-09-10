package com.polzzak_android.presentation.feature.term.model

import android.os.Parcelable
import androidx.annotation.StringRes
import com.polzzak_android.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class TermType(@StringRes val titleStrRes: Int, val url: String) : Parcelable {
    SERVICE(R.string.terms_of_service_detail_service_title, "https://www.polzzak.co.kr/terms "),
    PRIVACY(R.string.terms_of_service_detail_privacy_title, "https://www.polzzak.co.kr/privacy"),
}