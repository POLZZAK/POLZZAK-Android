package com.polzzak_android.presentation.feature.auth.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.polzzak_android.common.util.livedata.EventWrapper
import com.polzzak_android.common.util.safeLet
import com.polzzak_android.data.remote.model.ApiException
import com.polzzak_android.data.repository.GUIDRepository
import com.polzzak_android.data.repository.PushMessageRepository
import com.polzzak_android.data.repository.SignUpRepository
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.feature.auth.model.MemberTypeDetail.Companion.KID_TYPE_ID
import com.polzzak_android.presentation.feature.auth.model.SocialLoginType
import com.polzzak_android.presentation.feature.auth.signup.model.MemberTypeModel
import com.polzzak_android.presentation.feature.auth.signup.model.NickNameModel
import com.polzzak_android.presentation.feature.auth.signup.model.NickNameValidationState
import com.polzzak_android.presentation.feature.auth.signup.model.ProfileImageModel
import com.polzzak_android.presentation.feature.auth.signup.model.SignUpPage
import com.polzzak_android.presentation.feature.auth.signup.model.SignUpResultModel
import com.polzzak_android.presentation.feature.auth.signup.model.SignUpTermsOfServiceModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SignUpViewModel @AssistedInject constructor(
    private val signUpRepository: SignUpRepository,
    private val guidRepository: GUIDRepository,
    private val pushMessageRepository: PushMessageRepository,
    @Assisted private val userName: String?, @Assisted private val userType: SocialLoginType?
) : ViewModel() {
    private val _pageLiveData = MutableLiveData<SignUpPage>()
    val pageLiveData: LiveData<SignUpPage> = _pageLiveData

    private val _nickNameLiveData = MutableLiveData<NickNameModel>()
    val nickNameLiveData: LiveData<NickNameModel> = _nickNameLiveData

    private val _memberTypeLiveData = MutableLiveData<MemberTypeModel>()
    val memberTypeLiveData: LiveData<MemberTypeModel> = _memberTypeLiveData

    private val _profileImageLiveData = MutableLiveData<ProfileImageModel>(ProfileImageModel())
    val profileImageLiveData: LiveData<ProfileImageModel> = _profileImageLiveData

    private val _signUpResultLiveData =
        MutableLiveData<EventWrapper<ModelState<SignUpResultModel>>>()
    val signUpResultLiveData: LiveData<EventWrapper<ModelState<SignUpResultModel>>> =
        _signUpResultLiveData

    private val _termsOfServiceLiveData = MutableLiveData<SignUpTermsOfServiceModel>()
    val termsOfServiceLiveData: LiveData<SignUpTermsOfServiceModel> =
        _termsOfServiceLiveData

    private var checkNickNameValidationJob: Job? = null
    private var signUpJob: Job? = null

    init {
        _pageLiveData.value =
            safeLet(userName, userType) { _, _ -> SignUpPage.TermsOfService() }
                ?: SignUpPage.Error()
    }

    fun moveNextPage() {
        when (val page = pageLiveData.value) {
            is SignUpPage.TermsOfService -> _pageLiveData.value = SignUpPage.SelectType()
            is SignUpPage.SelectType -> _pageLiveData.value =
                if (memberTypeLiveData.value?.isKid() == true) SignUpPage.SetNickName(
                    progressCount = 1,
                    maxCount = 2
                ) else SignUpPage.SelectParentType(1, 3)

            is SignUpPage.SelectParentType -> _pageLiveData.value = SignUpPage.SetNickName(
                progressCount = page.progressCount + 1,
                maxCount = page.maxCount
            )


            is SignUpPage.SetNickName -> _pageLiveData.value = SignUpPage.SetProfileImage(
                progressCount = page.progressCount + 1,
                maxCount = page.maxCount
            )

            else -> {
                //do nothing
            }
        }
    }

    fun movePrevPage() {
        when (val page = pageLiveData.value) {
            is SignUpPage.SelectType -> _pageLiveData.value = SignUpPage.TermsOfService()
            is SignUpPage.SelectParentType -> _pageLiveData.value = SignUpPage.SelectType()
            is SignUpPage.SetNickName -> _pageLiveData.value =
                if (memberTypeLiveData.value?.isParent() == true) SignUpPage.SelectParentType(
                    page.progressCount - 1,
                    page.maxCount
                ) else SignUpPage.SelectType()

            is SignUpPage.SetProfileImage -> _pageLiveData.value =
                SignUpPage.SetNickName(page.progressCount - 1, page.maxCount)

            else -> {
                //do nothing
            }
        }
    }

    fun selectTypeParent() {
        if (memberTypeLiveData.value?.isParent() == true) return
        _memberTypeLiveData.value = MemberTypeModel(selectedType = MemberTypeModel.Type.PARENT)
    }

    fun selectTypeKid() {
        if (memberTypeLiveData.value?.isKid() == true) return
        _memberTypeLiveData.value =
            MemberTypeModel(
                selectedType = MemberTypeModel.Type.KID,
                selectedTypeId = KID_TYPE_ID
            )
    }

    fun selectParentType(selectedTypeId: Int?) {
        val memberTypeModel = memberTypeLiveData.value ?: MemberTypeModel()
        _memberTypeLiveData.value = memberTypeModel.copy(selectedTypeId = selectedTypeId)
    }

    fun setNickNameValue(nickName: String) {
        checkNickNameValidationJob?.cancel()
        val prevData = _nickNameLiveData.value ?: NickNameModel()
        _nickNameLiveData.value = NickNameModel(
            nickName = nickName,
            isEdited = prevData.isEdited || nickName.isNotEmpty()
        )
    }

    fun setProfileImagePath(path: String?) {
        _profileImageLiveData.value = ProfileImageModel(path = path)
    }

    fun requestCheckNickNameValidation() {
        if (checkNickNameValidationJob?.isCompleted == false) return
        checkNickNameValidationJob = viewModelScope.launch {
            val nickNameUiModel = nickNameLiveData.value ?: return@launch
            val nickName = nickNameUiModel.nickName ?: return@launch
            signUpRepository.requestCheckNickNameValidation(nickName = nickName)
                .onSuccess {
                    _nickNameLiveData.value =
                        nickNameUiModel.copy(nickNameState = NickNameValidationState.Valid)
                }
                .onError { exception, _ ->
                    when (exception) {
                        is ApiException.BadRequest -> {
                            _nickNameLiveData.value =
                                nickNameUiModel.copy(nickNameState = NickNameValidationState.Invalid)
                        }

                        else -> {
                            _nickNameLiveData.value =
                                nickNameUiModel.copy(
                                    nickNameState = NickNameValidationState.Error(
                                        exception = exception
                                    )
                                )
                        }
                    }
                }
        }
    }

    fun requestSignUp() {
        if (signUpJob?.isCompleted == false) return
        signUpJob = viewModelScope.launch {
            setSignUpResultLoading()
            safeLet(
                userName,
                memberTypeLiveData.value?.selectedTypeId,
                userType,
                nickNameLiveData.value?.nickName
            ) { userName, memberTypeId, userType, nickName ->
                signUpRepository.requestSignUp(
                    userName = userName,
                    memberTypeId = memberTypeId,
                    socialType = userType,
                    nickName = nickName,
                    profileImagePath = profileImageLiveData.value?.path
                ).onSuccess { signUpResponseData ->
                    signUpResponseData?.accessToken?.let {
                        val signUpResultModel =
                            SignUpResultModel(accessToken = it, memberTypeId = memberTypeId)
                        requestSendToken(signUpResultModel = signUpResultModel)
                    } ?: run {
                        setSignUpResultError()
                    }
                }.onError { exception, _ ->
                    setSignUpResultError(exception = exception)
                }
            }
        }
    }

    private suspend fun requestSendToken(
        signUpResultModel: SignUpResultModel
    ) {
        pushMessageRepository.requestPushToken().onSuccess {
            it ?: run {
                setSignUpResultError(exception = ApiException.FirebaseTokenFailed())
                return@onSuccess
            }
            pushMessageRepository.requestPostPushToken(
                accessToken = signUpResultModel.accessToken,
                token = it
            ).onSuccess {
                setSignUpResultSuccess(data = signUpResultModel)
            }.onError { exception, _ ->
                setSignUpResultError(exception = exception)
            }
        }.onError { exception, _ ->
            setSignUpResultError(exception = exception)
        }
    }

    fun checkTermsOfService(clickModel: SignUpTermsOfServiceModel.ClickModel) {
        val model = termsOfServiceLiveData.value ?: SignUpTermsOfServiceModel()
        _termsOfServiceLiveData.value = when (clickModel) {
            SignUpTermsOfServiceModel.ClickModel.SERVICE -> model.copy(isCheckedService = !model.isCheckedService)
            SignUpTermsOfServiceModel.ClickModel.PRIVACY -> model.copy(isCheckedPrivacy = !model.isCheckedPrivacy)
            SignUpTermsOfServiceModel.ClickModel.ALL -> {
                val isChecked = !(model.isCheckedService && model.isCheckedPrivacy)
                SignUpTermsOfServiceModel(
                    isCheckedService = isChecked,
                    isCheckedPrivacy = isChecked
                )
            }
        }
    }

    private fun setSignUpResultLoading() {
        _signUpResultLiveData.value = EventWrapper(ModelState.Loading())
    }

    private fun setSignUpResultSuccess(data: SignUpResultModel) {
        _signUpResultLiveData.value = EventWrapper(ModelState.Success(data = data))

    }

    private fun setSignUpResultError(exception: Exception = ApiException.UnknownError()) {
        _signUpResultLiveData.value = EventWrapper(ModelState.Error(exception = exception))
    }

    @AssistedFactory
    interface SignUpAssistedFactory {
        fun create(userName: String?, userType: SocialLoginType?): SignUpViewModel
    }

    companion object {
        fun provideFactory(
            signUpAssistedFactory: SignUpAssistedFactory,
            userName: String?,
            userType: SocialLoginType?
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return signUpAssistedFactory.create(userName = userName, userType = userType) as T
            }
        }
    }
}