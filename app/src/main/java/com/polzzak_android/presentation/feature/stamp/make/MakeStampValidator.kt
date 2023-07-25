package com.polzzak_android.presentation.feature.stamp.make

object MakeStampValidator {

    fun checkName(name: String?): Pair<Boolean, String> {
        if (name == null || name == "") {
            return Pair(false, "도장판 이름을 입력해주세요")
        }

        if (name.length > 20) {
            return Pair(false, "20자까지만 쓸 수 있어요")
        }

        return Pair(true, "도장판 이름 유효성 체크 통과")
    }

    fun checkReward(reward: String?) : Pair<Boolean, String> {
        if (reward == null || reward == "") {
            return Pair(false, "보상을 입력해주세요")
        }

        if (reward.length > 30) {
            return Pair(false, "30자까지만 쓸 수 있어요")
        }

        return Pair(true, "도장판 보상 유효성 체크 통과")
    }

    fun checkCount(count: Int?) : Pair<Boolean, String> {
        if (count == null || count == 0) {
            return Pair(false, "도장 개수를 선택해주세요")
        }

        return Pair(true, "도장판 개수 유효성 체크 통과")
    }

    data class ResultMissionValidate(
        val message: String,
        val index: Int
    )

    fun checkMission(missionList: List<String>?) : Pair<Boolean, ResultMissionValidate> {
        if (missionList == null) {
            return Pair(false, ResultMissionValidate("도장판 미션 리스트 비었음", 0))
        }

        for (mission in missionList) {
            val index = missionList.indexOf(mission)

            if (mission.length > 20) {
                return Pair(false, ResultMissionValidate("20자까지만 쓸 수 있어요", index))
            }

            if (mission == "") {
                return Pair(false, ResultMissionValidate("빈칸이 있어요!", index))
            }

            val count = missionList.count { it == mission }
            if (count >= 2) {
                return Pair(false, ResultMissionValidate("동일한 이름의 미션이 있어요", index))
            }
        }

        return Pair(true, ResultMissionValidate("도정판 미션 유효성 체크 통과", 0))
    }
}