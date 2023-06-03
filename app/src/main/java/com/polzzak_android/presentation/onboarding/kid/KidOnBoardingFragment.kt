package com.polzzak_android.presentation.onboarding.kid

import com.polzzak_android.presentation.onboarding.base.BaseOnBoardingFragment
import com.polzzak_android.presentation.onboarding.model.OnBoardingPageModel

class KidOnBoardingFragment : BaseOnBoardingFragment() {
    //TODO string resource 적용
    override val pageData: List<OnBoardingPageModel> = listOf(
        OnBoardingPageModel(
            title = "폴짝!\n회원이 되신 것을 환영해요",
            content = "칭찬 도장을 모으며\n폴짝! 성장해봐요"
        ),
        OnBoardingPageModel(
            title = "먼저 보호자와 연동이 필요해요!",
            content = "보호자와 연동해야\n도장판을 받을 수 있어요"
        ),
        OnBoardingPageModel(
            title = "보호자와 함께\n도장판을 만들어보세요!",
            content = "갖고 싶은 선물을 정하고\n선물을 얻기 위한 미션을 정해보세요"
        ),
        OnBoardingPageModel(
            title = "도장판을 다 모으면\n선물 쿠폰이 발행돼요!",
            content = "수고했어요.\n원하는 게 있다면 도장판을 또 생성해봐요!"
        ),
        OnBoardingPageModel(
            title = "누가누가\n제일 높이 올라갈까!",
            content = "포인트를 모아서\n계단을 폴짝! 올라갈 수 있어요"
        )
    )
}