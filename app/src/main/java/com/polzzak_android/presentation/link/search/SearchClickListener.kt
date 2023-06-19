package com.polzzak_android.presentation.link.search

import com.polzzak_android.presentation.link.model.LinkUserModel

//TODO 연동관리 화면이랑 공용으로 사용하도록 변경 or 클릭리스너 분리
interface SearchClickListener {
    //연동 요청 취소 다이얼로그
    fun displayCancelRequestDialog(linkUserModel: LinkUserModel)

    //연동 요청 다이얼로그
    fun displayRequestLinkDialog(linkUserModel: LinkUserModel)

    //연동 요청 취소
    fun cancelRequestLink(linkUserModel: LinkUserModel)

    //검색 취소
    fun cancelSearch()
}