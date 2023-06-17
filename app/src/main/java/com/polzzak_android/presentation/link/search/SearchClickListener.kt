package com.polzzak_android.presentation.link.search

interface SearchClickListener {
    //연동 요청 취소 다이얼로그
    fun displayCancelRequestDialog(nickName: String, targetId: Int)

    //연동 요청 다이얼로그
    fun displayRequestLinkDialog(nickName: String, targetId: Int)

    //연동 요청 취소
    fun cancelRequestLink(targetId: Int)

    //검색 취소
    fun cancelSearch()
}