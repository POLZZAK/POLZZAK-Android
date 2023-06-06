package com.polzzak_android.presentation.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polzzak_android.common.util.livedata.EventWrapper
import com.polzzak_android.data.repository.LoginRepository
import com.polzzak_android.data.repository.MemberTypeRepository
import com.polzzak_android.data.repository.UserRepository
import com.polzzak_android.presentation.auth.login.model.LoginConvertor.toSocialLoginType
import com.polzzak_android.presentation.auth.login.model.LoginInfoUiModel
import com.polzzak_android.presentation.auth.model.SocialLoginType
import com.polzzak_android.presentation.common.model.ApiResult
import com.polzzak_android.presentation.auth.signup.model.MemberTypeDetail
import com.polzzak_android.presentation.auth.signup.model.MemberTypeDetail.Companion.KID_TYPE_ID
import com.polzzak_android.presentation.common.util.toApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val memberTypeRepository: MemberTypeRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _loginInfoLiveData = MutableLiveData<EventWrapper<ApiResult<LoginInfoUiModel>>>()
    val loginInfoLiveData: LiveData<EventWrapper<ApiResult<LoginInfoUiModel>>> = _loginInfoLiveData

    private var loginJob: Job? = null

    fun requestGoogleLogin(authCode: String) {
        if (loginJob?.isCompleted == false) return
        loginJob = viewModelScope.launch {
            val googleOAuthTokensDeferred =
                async { loginRepository.requestGoogleAccessToken(authCode = authCode) }
            val googleOAuthResponse = googleOAuthTokensDeferred.await()
            val accessToken = googleOAuthResponse.body()?.accessToken
            accessToken?.let {
                requestLogin(accessToken = it, loginType = SocialLoginType.GOOGLE)
            } ?: run {
                Timber.d("google access token 발급 실패")
                //TODO access token 발급 실패 callback
            }
        }
    }

    fun requestKakaoLogin(accessToken: String) {
        if (loginJob?.isCompleted == false) return
        loginJob = viewModelScope.launch {
            requestLogin(accessToken = accessToken, loginType = SocialLoginType.KAKAO)
        }
    }

    private suspend fun requestLogin(accessToken: String, loginType: SocialLoginType) {
        coroutineScope {
            _loginInfoLiveData.value = EventWrapper(ApiResult.loading())

            val loginDeferred = async {
                loginRepository.requestLogin(
                    accessToken = accessToken,
                    loginType = loginType
                )
            }
            val memberTypeDeferred = async { memberTypeRepository.requestMemberTypes() }

            val loginResponse = loginDeferred.await()
            val memberTypeResponse = memberTypeDeferred.await()

            /*
                TODO 에러케이스 정리 후 케이스 분기 처리(Result로 받은 뒤 success error 처리하는 방향으로 갈 예정)
                아래 코드는 결과 출력용 임시 코드
            */
            val loginModel = loginResponse.toApiResult {
                Triple(
                    it?.userName,
                    it?.socialType?.toSocialLoginType(),
                    it?.accessToken ?: ""
                )
            }
            val memberTypeModel = memberTypeResponse.toApiResult {
                it?.memberTypeDetailList?.map { memberTypeResponse ->
                    if (memberTypeResponse.memberTypeDetailId == KID_TYPE_ID) MemberTypeDetail.Kid(
                        memberTypeResponse.memberTypeDetailId,
                        memberTypeResponse.detail
                    ) else MemberTypeDetail.Parent(
                        memberTypeResponse.memberTypeDetailId,
                        memberTypeResponse.detail
                    )
                }
            }
            when {
                loginModel is ApiResult.Success -> {
                    //로그인 성공 시
                    //TODO 유저정보 요청(타입 분기처리)
                    val loginInfoUiModel = LoginInfoUiModel(accessToken = loginModel.data?.third)
                    _loginInfoLiveData.value =
                        EventWrapper(ApiResult.success(loginInfoUiModel))
                }

                loginModel is ApiResult.Error && memberTypeModel is ApiResult.Success && loginModel.code == 412 -> {
                    //회원가입 필요
                    val loginInfoUiModel = LoginInfoUiModel(
                        userName = loginModel.data?.first,
                        socialType = loginModel.data?.second,
                        parentTypes = memberTypeModel.data?.filterIsInstance<MemberTypeDetail.Parent>()
                    )
                    _loginInfoLiveData.value = EventWrapper(ApiResult.success(loginInfoUiModel))
                }

                else -> {
                    _loginInfoLiveData.value =
                        EventWrapper(ApiResult.Error(statusCode = 401, code = null))
                }
            }

        }
    }
}