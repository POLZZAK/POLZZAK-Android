package com.polzzak_android.presentation.feature.myPage.notice

import com.polzzak_android.presentation.feature.myPage.notice.model.MyNoticeModel

interface MyNoticeClickListener {
    fun onClickNotice(model: MyNoticeModel)
}