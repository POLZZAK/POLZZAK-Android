package com.polzzak_android.presentation.onboarding.protector

import com.polzzak_android.presentation.onboarding.base.BaseOnBoardingFragment
import com.polzzak_android.presentation.onboarding.model.OnBoardingPageModel

class ProtectorOnBoardingFragment : BaseOnBoardingFragment() {
    //TODO string resource 적용
    override val pageData: List<OnBoardingPageModel> = listOf(
        OnBoardingPageModel(
            title = "폴짝!\n회원이 되신 것을 환영해요",
            content = "칭찬 도장을 모으며 \n폴짝! 성장할 아이를 기대해주세요"
        ),
        OnBoardingPageModel(
            title = "먼저 아이와 연동이 필요해요!",
            content = "아이와 연동을 해야 \n도장판을 만들어 줄 수 있어요"
        ),
        OnBoardingPageModel(
            title = "아이와 함께\n도장판을 만들어보세요!",
            content = "아이가 갖고 싶은 선물을 정하고\n함께 미션을 만들어보세요."
        ),
        OnBoardingPageModel(
            title = "도장판을 다 모으면\n선물 쿠폰을 발행해주세요!",
            content = "아이가 열심히 노력했어요.\n또 다른 도장판으로 동기부여를 해주세요!"
        ),
        OnBoardingPageModel(
            title = "열심히 활동하면\n포인트가 쌓여요!",
            content = "아이와 열심히 활동하면\n계단을 폴짝! 올라갈 수 있어요"
        )
    )
}