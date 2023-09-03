package com.polzzak_android.presentation.feature.myPage.model

fun getFAQListForProtector(): List<FAQModel> {
    return listOf(
        FAQModel(
            type = FAQType.STAMP,
            title = "진행 중인 도장판과, 완료된 도장판의 차이가 무엇인가요?",
            content = "진행 중인 도장판은 말그대로 아직 도장을 다 모으지 않은 도장판입니다. 완료된 도장판은 해당 도장판에 도장을 다 모은 뒤 보호자님이 선물 쿠폰 발급하고 마지막으로 아이가 선물 쿠폰을 받는 것까지 완료된 도장판 입니다."
        ),
        FAQModel(
            type = FAQType.STAMP,
            title = "도장판은 보호자만 만들 수 있나요?",
            content = "네 그렇습니다. 도장판은 보호자만 만들 수 있습니다. 대신 도장판 제작 시 아이와 함께 만들어주세요!"
        ),
        FAQModel(
            type = FAQType.STAMP,
            title = "도장판 수정은 보호자만 할 수 있나요?",
            content = "네 그렇습니다. 제작과 마찬가지로 수정도 보호자님만 할 수 있습니다. 그렇지만 아이와 상의하지 않고 보상이나 미션을 맘대로 바꾸지 않아야 합니다."
        ),
        FAQModel(
            type = FAQType.STAMP,
            title = "도장 찍기는 보호자만 가능한가요?",
            content = "네 그렇습니다. 도장은 보호자만 찍어줄 수 있습니다."
        ),
        FAQModel(
            type = FAQType.STAMP,
            title = "아이의 도장요청은 무엇인가요?",
            content = "보호자님이 일일이 아이가 미션을 수행했는지 지켜볼 수 없기 때문에, 아이가 미션을 수행했다고 도장을 찍어달라고 보호자님에게 요청할 수 있습니다. 진짜로 수행했는지는 보호자님의 판단이 중요합니다! 거짓말을 한 것이었다면, 도장 요청이 있는 해당 도장판에서 요청을 거절할 수 있습니다."
        ),
        FAQModel(
            type = FAQType.STAMP,
            title = "아이의 도장요청은 어디서 볼 수 있나요?",
            content = "특정 도장판의 빈 도장을 클릭하면, 해당 도장판에 들어온 도장 요청들을 볼 수 있습니다."
        ),
        FAQModel(
            type = FAQType.COUPON,
            title = "선물 쿠폰은 언제 줄 수 있나요?",
            content = "도장판에 있는 도장을 다 모은 경우, 선물 쿠폰 발급 버튼이 자동으로 활성화됩니다. 도장을 다 모은 아이를 위해 쿠폰을 꼭 선물해주세요!"
        ),
        FAQModel(
            type = FAQType.COUPON,
            title = "선물 전 쿠폰은 무엇인가요?",
            content = "아이가 도장판을 다 모아서 발급해준 선물 쿠폰에 대해, 보호자님이 아직 실제로 선물을 전달하지 않은 쿠폰입니다."
        ),
        FAQModel(
            type = FAQType.COUPON,
            title = "선물 완료 쿠폰은 무엇인가요?",
            content = "아이가 도장판을 다 모아서 발급해준 선물 쿠폰에 대해, 보호자님이 실제로 선물을 전달해준 쿠폰입니다. 아이가 선물을 받고 ‘선물 받기 완료' 버튼을 눌러줘야 ‘선물 완료’ 쿠폰으로 구분돼요."
        ),
        FAQModel(
            type = FAQType.COUPON,
            title = "약속한 날짜 안에 아이에게 선물을 지급하지 않으면 어떻게 되나요?",
            content = "선물 약속을 어기게 되면 100P가 차감됩니다. 선물 약속은 꼭 지켜주세요!"
        ),
        FAQModel(
            type = FAQType.ACCOUNT,
            title = "계정을 여러 개 만들 수 있나요?",
            content = "현재 저희 서비스는 카카오톡, 구글, 애플 소셜 로그인을 지원하고 있습니다. 각각의 소셜 아이디별로 한 개씩 계정을 생성할 수 있어요."
        ),
        FAQModel(
            type = FAQType.ACCOUNT,
            title = "보호자 회원으로 가입했는데 아이 회원으로 변경할 수 없나요?",
            content = "이미 보호자 회원으로 가입한 이상 아이 회원으로 변경할 수 없습니다. 실수로 잘못 가입했다면 회원탈퇴 절차를 거쳐 다시 가입해주세요. 또한 다른 소셜 계정을 이용해 아이 회원으로 가입하는 방법도 있습니다."
        ),
        FAQModel(
            type = FAQType.ACCOUNT,
            title = "가족관계는 수정할 수 없나요?",
            content = "네, 가족관계는 수정할 수 없습니다. 다른 가족관계로 활동을 하고싶다면 다른 소셜 계정으로 가입해주세요."
        ),
        FAQModel(
            type = FAQType.ACCOUNT,
            title = "회원 탈퇴 후에는 개인 정보가 모두 삭제되나요?",
            content = "회원 탈퇴와 함께 회원님의 개인정보가 모두 삭제됩니다. 또한 만들었던 도장판, 선물 받은 쿠폰들, 연동 목록까지 모두 삭제되니 탈퇴에 유의해주세요."
        ),
        FAQModel(
            type = FAQType.POINT,
            title = "포인트가 안 들어왔어요.",
            content = "죄송합니다. 오류로 인해 포인트가 적립되지 않은 것 같아요. 포인트가 누락된 것에 대해서는 직접 1:1 문의를 넣어주시는 분에 한해 수동으로 포인트를 넣어드리고 있습니다. 하단 1:1 문의하기 배너를 통해 들어오셔서 포인트 관련 문의를 넣어주시길 바랍니다.\n" +
                    "문의를 넣어주실 때 어떤 종류의 포인트가 누락되었는지 꼭 적어주세요.(ex. 아이 한 명과 연동했는데 연동 포인트가 안 들어왔어요) 그렇지 않으면 누락 포인트 확인 절차를 거쳐야 해서 포인트 지급이 오래걸릴 수 있어요."
        ),
        FAQModel(
            type = FAQType.POINT,
            title = "모은 포인트는 어디에 사용할 수 있나요?",
            content = "아쉽게도 현재 저희 서비스 내에서 모은 포인트를 사용할 수 있는 곳은 없습니다. 다만 열심히 모은 포인트로 회원 랭킹을 높일 수 있어요. 많은 포인트를 쌓아서 랭킹에 도전해보세요!"
        ),
        FAQModel(
            type = FAQType.POINT,
            title = "계단이 무엇인가요?",
            content = "‘계단’은 폴짝에서 레벨을 칭하는 단어입니다. 1계단은 레벨 1을 뜻하며 100포인트를 모을 때마다 1계단씩 올라갈 수 있어요!"
        ),
        FAQModel(
            type = FAQType.LINKED,
            title = "아이와 연동하려고 하는데 아이가 검색이 안 돼요.",
            content = "1. 아이가 폴짝에 가입되어 있는지 확인 부탁드립니다. \n" +
                    "2. 아이의 닉네임을 틀리지 않고 정확하게 입력했는지 확인 부탁드립니다.\n" +
                    "3. 아이가 ‘보호자 회원’으로 가입하지는 않았는지 확인 부탁드립니다. 아이가 보호자 회원으로 가입했다면 검색이 안 됩니다.\n" +
                    "4. 1,2,3번 상황에 해당되지 않는데도 검색이 되지 않는다면 1:1 문의 부탁드립니다."
        ),
        FAQModel(
            type = FAQType.LINKED,
            title = "아이에게 연동 요청을 보냈는데, 연동이 되지 않았어요.",
            content = "1. 아직 아이가 수락하지 않았을 수 있습니다. 잠시만 기다려주세요! \n" +
                    "2. 아이가 실수로 요청을 거절했을 수 있습니다. 아이에게 직접 확인 부탁드립니다. \n" +
                    "3. 1,2번 상황에 해당되지 않는다면 시스템 오류로 연동 요청이 안 보내졌을 수 있습니다. 한번 더 시도해주세요! 그래도 해결되지 않는다면, 1:1 문의 부탁드립니다."
        ),
        FAQModel(
            type = FAQType.LINKED,
            title = "아이가 연동 요청을 보내서 수락했는데, 연동이 되지 않았어요.",
            content = "1. 아이가 연동 요청을 취소했을 수 있어요. 아이에게 직접 확인 부탁드립니다.\n" +
                    "2. 시스템 오류로 연동에 실패했을 수 있어요. 다시 한번 연동 작업을 부탁드립니다.\n" +
                    "3. 1,2번 상황에 해당되지 않는데도 연동에 실패했다면, 1:1 문의 부탁드립니다."
        ),
        FAQModel(
            type = FAQType.LINKED,
            title = "연동 요청한 것들은 어디서 볼 수 있나요?",
            content = "메인홈 > 오른쪽 상단의 연동관리 아이콘에 들어가시면 연동을 보낸 것, 받은 것, 연동된 아이들까지 모두 볼 수 있습니다."
        ),
    )
}

fun getFAQListForKid(): List<FAQModel> {
    return listOf(
        FAQModel(
            type = FAQType.STAMP,
            title = "진행 중인 도장판과, 완료된 도장판의 차이가 무엇인가요?",
            content = "진행 중인 도장판은 말그대로 아직 도장을 다 모으지 않은 도장판입니다. 완료된 도장판은 해당 도장판에 도장을 다 모은 뒤 보호자가 선물 쿠폰 발급하고 마지막으로 아이가 선물 쿠폰을 받는 것까지 완료된 도장판 입니다."
        ),
        FAQModel(
            type = FAQType.STAMP,
            title = "아이회원은 도장판을 만들 수 없나요?",
            content = "네 그렇습니다. 도장판은 보호자만 만들 수 있습니다. 아무래도 선물을 줘야 되는 사람은 보호자님이기 때문에 보호자와 꼭 상의를 해서 도장판을 만들었으면 하는 바람에 도장판 제작 권한은 보호자에게만 부여하였습니다 ㅠㅠ"
        ),
        FAQModel(
            type = FAQType.STAMP,
            title = "아이회원은 도장판을 수정할 수 없나요?",
            content = "네 그렇습니다. 도장판 수정은 보호자만 가능합니다. 보호자가 미션과 보상을 상의없이 맘대로 바꾸지 않는지 눈 크게 뜨고 지켜봐주세요"
        ),
        FAQModel(
            type = FAQType.STAMP,
            title = "도장 요청은 어떻게 할 수 있나요?",
            content = "도장을 요청하고 싶은 특정 도장판에 들어갑니다 > 빈 도장을 클릭합니다 > 도장 요청이 가능합니다"
        ),
        FAQModel(
            type = FAQType.COUPON,
            title = "선물 쿠폰은 언제 받을 수 있어요? ",
            content = "도장판을 다 모으고 나면 보호자님이 쿠폰을 발급해줄 거에요. 열심히 도장을 모아보세요!"
        ),
        FAQModel(
            type = FAQType.COUPON,
            title = "선물 전 쿠폰은 무엇인가요?",
            content = "도장판을 다 모아서 받은 선물 쿠폰에 대해, 보호자가 아직 실제로 선물을 전달하지 않은 쿠폰입니다."
        ),
        FAQModel(
            type = FAQType.COUPON,
            title = "선물 완료 쿠폰은 무엇인가요?",
            content = "도장판을 다 모아서 받은 선물 쿠폰에 대해, 보호자가 실제로 선물을 전달해준 쿠폰입니다. 받은 쿠폰에 있는 ‘선물 받기 완료’ 버튼을 눌러줘야 ‘선물 받기 완료’ 쿠폰으로 구분돼요. 선물을 실제로 전달받았다면 버튼을 꼭 눌러주세요!"
        ),
        FAQModel(
            type = FAQType.COUPON,
            title = "‘선물 조르기’는 뭔가요?",
            content = "보호자님이 언제 선물을 전달해주겠다고 약속은 하였지만, 얼른 선물을 받고 싶은 경우 선물을 달라고 조를 수 있어요!"
        ),
        FAQModel(
            type = FAQType.COUPON,
            title = "‘선물 받기 완료’ 버튼은 뭔가요?",
            content = "특정 선물 쿠폰에 대해서 보호자님에게 실제 선물을 전달받은 경우 눌러줘야 합니다. 선물을 약속한 기간 내에 버튼을 눌러주지 않으면 보호자님의 포인트가 100포인트 깎여요! \n" +
                    "그러나 선물을 전달받지 못했다면 눌러주지 않아도 됩니다. 보호자님에게 -100P의 벌을 내려주세요! \n" +
                    "약속한 날짜 안에 선물을 못 받고 나중에 받았다하더라도 버튼은 꼭 눌러주세요. 그래야 ‘선물 완료 쿠폰’으로 구분돼요."
        ),
        FAQModel(
            type = FAQType.ACCOUNT,
            title = "계정을 여러 개 만들 수 있나요?",
            content = "현재 저희 서비스는 카카오톡, 구글, 애플 소셜 로그인을 지원하고 있습니다. 각각의 소셜 아이디별로 한 개씩 계정을 생성할 수 있어요"
        ),
        FAQModel(
            type = FAQType.ACCOUNT,
            title = "아이 회원으로 가입했는데 보호자 회원으로 변경할 수 없나요?",
            content = "이미 아이 회원으로 가입한 이상 보호자 회원으로 변경할 수 없습니다. 실수로 잘못 가입했다면 회원탈퇴 절차를 거쳐 다시 가입해주세요. 또한 다른 소셜 계정을 이용해 보호자 회원으로 가입하는 방법도 있습니다."
        ),
        FAQModel(
            type = FAQType.ACCOUNT,
            title = "회원 탈퇴 후에는 개인 정보가 모두 삭제되나요?",
            content = "회원 탈퇴와 함께 회원님의 개인정보가 모두 삭제됩니다. 또한 만들었던 도장판, 선물 받은 쿠폰들, 연동 목록까지 모두 삭제되니 탈퇴에 유의해주세요."
        ),
        FAQModel(
            type = FAQType.POINT,
            title = "포인트가 안 들어왔어요.",
            content = "죄송합니다. 오류로 인해 포인트가 적립되지 않은 것 같아요. 포인트가 누락된 것에 대해서는 직접 1:1 문의를 넣어주시는 분에 한해 수동으로 포인트를 넣어드리고 있습니다. 하단 1:1 문의하기 배너를 통해 들어오셔서 포인트 관련 문의를 넣어주시길 바랍니다.\n" +
                    "문의를 넣어주실 때 어떤 종류의 포인트가 누락되었는지 꼭 적어주세요. (ex. 보호자 한 명과 연동했는데 연동 포인트가 안 들어왔어요). 그렇지 않으면 누락 포인트 확인 절차를 거쳐야 해서 포인트 지급이 오래걸릴 수 있어요."
        ),
        FAQModel(
            type = FAQType.POINT,
            title = "모은 포인트는 어디에 사용할 수 있나요?",
            content = "아쉽게도 현재 저희 서비스 내에서 모은 포인트를 사용할 수 있는 곳은 없습니다. 다만 열심히 모은 포인트로 회원 랭킹을 높일 수 있어요. 많은 포인트를 쌓아서 랭킹에 도전해보세요!"
        ),
        FAQModel(
            type = FAQType.POINT,
            title = "계단이 무엇인가요?",
            content = "‘계단’은 폴짝에서 레벨을 칭하는 단어입니다. 1계단은 레벨 1을 뜻하며 100포인트를 모을 때마다 1계단씩 올라갈 수 있어요!"
        ),
        FAQModel(
            type = FAQType.LINKED,
            title = "보호자와 연동하고 싶은데 보호자가 검색이 안 돼요.",
            content = "1. 보호자가 폴짝에 가입되어 있는지 확인 부탁드립니다. \n" +
                    "2. 보호자의 닉네임을 틀리지 않고 정확하게 입력했는지 확인 부탁드립니다.\n" +
                    "3. 보호자가 ‘아이 회원’으로 가입하지는 않았는지 확인 부탁드립니다. 아이가 보호자 회원으로 가입했다면 검색이 안 됩니다.\n" +
                    "3. 1,2,3 번 상황에 해당되지 않는데도 검색이 되지 않는다면 1:1 문의 부탁드립니다"
        ),
        FAQModel(
            type = FAQType.LINKED,
            title = "보호자에게 연동 요청을 보냈는데, 연동이 되지 않았어요.",
            content = "1.아직 보호자가 수락하지 않았을 수 있습니다. 잠시만 기다려주세요! \n" +
                    "2. 보호자가 실수로 요청을 거절했을 수 있습니다. 보호자님에게 직접 확인 부탁드립니다. \n" +
                    "3. 1,2번 상황에 해당되지 않는데도 해결되지 않는다면, 1:1 문의 부탁드립니다."
        ),
        FAQModel(
            type = FAQType.LINKED,
            title = "보호자가 연동 요청을 보내서 수락했는데, 연동이 되지 않았어요.",
            content = "1. 보호자가 연동 요청을 취소했을 수 있어요. 보호자님에게 직접 확인 부탁드립니다.\n" +
                    "2. 시스템 오류로 연동에 실패했을 수 있어요. 다시 한번 연동 작업을 부탁드립니다.\n" +
                    "3. 1,2번 상황에 해당되지 않는데도 연동에 실패했다면, 1:1 문의 부탁드립니다."
        ),
        FAQModel(
            type = FAQType.LINKED,
            title = "연동 요청한 것들은 어디서 볼 수 있나요? ",
            content = "메인홈 > 오른쪽 상단의 연동관리 아이콘에 들어가시면 연동을 보낸 것, 받은 것, 연동된 보호자들까지 모두 볼 수 있습니다."
        ),
    )
}