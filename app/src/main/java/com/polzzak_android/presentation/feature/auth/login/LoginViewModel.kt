package com.polzzak_android.presentation.feature.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.polzzak_android.common.util.livedata.EventWrapper
import com.polzzak_android.common.util.safeLet
import com.polzzak_android.data.remote.model.ApiException
import com.polzzak_android.data.repository.GUIDRepository
import com.polzzak_android.data.repository.LoginRepository
import com.polzzak_android.data.repository.MemberTypeRepository
import com.polzzak_android.data.repository.UserRepository
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.model.asMemberTypeOrNull
import com.polzzak_android.presentation.feature.auth.login.model.LoginInfoModel
import com.polzzak_android.presentation.feature.auth.model.MemberTypeDetail
import com.polzzak_android.presentation.feature.auth.model.SocialLoginType
import com.polzzak_android.presentation.feature.auth.model.asMemberTypeDetail
import com.polzzak_android.presentation.feature.auth.model.asSocialLoginTypeOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val memberTypeRepository: MemberTypeRepository,
    private val userRepository: UserRepository,
    private val guidRepository: GUIDRepository
) : ViewModel() {
    private val _loginInfoLiveData = MutableLiveData<EventWrapper<ModelState<LoginInfoModel>>>()
    val loginInfoLiveData: LiveData<EventWrapper<ModelState<LoginInfoModel>>> = _loginInfoLiveData

    private var loginJob: Job? = null

    fun requestGoogleLogin(authCode: String) {
        if (loginJob?.isCompleted == false) return
        loginJob = viewModelScope.launch {
            val googleOAuthTokensDeferred =
                async { loginRepository.requestGoogleAccessToken(authCode = authCode) }
            val googleOAuthResponse = googleOAuthTokensDeferred.await()
            val accessToken = googleOAuthResponse.data?.accessToken
            accessToken?.let {
                requestLogin(accessToken = it, loginType = SocialLoginType.GOOGLE)
            } ?: run {
                setLoginResultError()
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
        loginRepository.requestLogin(accessToken = accessToken, loginType = loginType)
            .onSuccess {
                requestLoginInfo(accessToken = it?.accessToken ?: "", socialType = loginType)
            }.onError { exception, loginResponseData ->
                when (exception) {
                    is ApiException.RequiredRegister -> {
                        val userName = loginResponseData?.userName
                        val socialType =
                            asSocialLoginTypeOrNull(loginResponseData?.socialType ?: "")
                        safeLet(userName, socialType) { _userName, _socialType ->
                            requestParentTypes(userName = _userName, socialType = _socialType)
                        } ?: run {
                            setLoginResultError()
                        }
                    }

                    else -> {
                        setLoginResultError(exception = exception)
                    }
                }
            }
    }

    private suspend fun requestLoginInfo(accessToken: String, socialType: SocialLoginType) {
        setLoginResultLoading()
        userRepository.requestUser(accessToken = accessToken).onSuccess {
            val memberType = it?.memberType?.let { memberTypeResponseData ->
                asMemberTypeOrNull(memberTypeResponseData)
            } ?: run {
                setLoginResultError()
                return@onSuccess
            }
            val loginInfoModel =
                LoginInfoModel.Login(
                    accessToken = accessToken,
                    memberType = memberType,
                    socialType = socialType
                )
            requestSendToken(loginInfoModel = loginInfoModel)
//            setLoginResultSuccess(data = loginInfoModel)
        }.onError { exception, _ ->
            setLoginResultError(exception = exception)
        }
    }

    //TODO 푸시알림 토큰 등록
    private suspend fun requestSendToken(loginInfoModel: LoginInfoModel) {
        val guid = guidRepository.requestGUID()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.w(task.exception, "Fetching FCM registration token failed")
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            Timber.d( "token : $token\nguid : $guid")
            //TODO 서버 토큰 등록
            setLoginResultSuccess(data = loginInfoModel)
        })
    }

    private suspend fun requestParentTypes(userName: String, socialType: SocialLoginType) {
        memberTypeRepository.requestMemberTypes().onSuccess {
            val parentTypes = it?.memberTypeDetailList?.map { responseData ->
                asMemberTypeDetail(memberTypeDetailResponseData = responseData)
            }?.filterIsInstance<MemberTypeDetail.Parent>() ?: emptyList()
            val loginInfoModel =
                LoginInfoModel.SignUp(
                    userName = userName,
                    socialType = socialType,
                    parentTypes = parentTypes
                )
            setLoginResultSuccess(data = loginInfoModel)
        }.onError { exception, _ ->
            setLoginResultError(exception = exception)
        }
    }

    private fun setLoginResultLoading() {
        _loginInfoLiveData.value = EventWrapper(ModelState.Loading())
    }

    private fun setLoginResultSuccess(data: LoginInfoModel) {
        _loginInfoLiveData.value = EventWrapper(ModelState.Success(data = data))
    }

    private fun setLoginResultError(exception: Exception = ApiException.UnknownError()) {
        _loginInfoLiveData.value = EventWrapper(ModelState.Error(exception = exception))
    }
}