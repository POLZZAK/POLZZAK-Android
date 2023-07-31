package com.polzzak_android.presentation.feature.link

import com.polzzak_android.presentation.feature.link.model.LinkUserModel

interface LinkClickListener {
    //연동 요청 취소 다이얼로그
    fun displayCancelRequestDialog(linkUserModel: LinkUserModel) {}

    //연동 요청 다이얼로그
    fun displayRequestLinkDialog(linkUserModel: LinkUserModel) {}

    //연동 삭제 다이얼로그
    fun displayDeleteLinkDialog(linkUserModel: LinkUserModel) {}

    //연동 요청 수락 다이얼로그
    fun displayApproveRequestDialog(linkUserModel: LinkUserModel) {}

    //연동 요청 거절 다이얼로그
    fun displayRejectRequestDialog(linkUserModel: LinkUserModel) {}

    //연동 요청 취소
    fun cancelRequestLink(linkUserModel: LinkUserModel) {}

    //검색 취소
    fun cancelSearch() {}
}