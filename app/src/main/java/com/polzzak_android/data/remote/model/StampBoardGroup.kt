package com.polzzak_android.data.remote.model

/**
 * 도장판 목록 조회 요청 시 넘겨야 하는 목록 종류
 */
enum class StampBoardGroup(val value: String) {
    IN_PROGRESS("in_progress"),
    ENDED("ended")
}