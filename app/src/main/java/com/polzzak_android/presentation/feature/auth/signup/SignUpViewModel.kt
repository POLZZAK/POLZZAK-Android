package com.polzzak_android.presentation.feature.auth.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.polzzak_android.common.util.livedata.EventWrapper
import com.polzzak_android.common.util.safeLet
import com.polzzak_android.data.remote.model.ApiException
import com.polzzak_android.data.repository.SignUpRepository
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.feature.auth.model.MemberTypeDetail.Companion.KID_TYPE_ID
import com.polzzak_android.presentation.feature.auth.model.SocialLoginType
import com.polzzak_android.presentation.feature.auth.signup.model.MemberTypeUiModel
import com.polzzak_android.presentation.feature.auth.signup.model.NickNameUiModel
import com.polzzak_android.presentation.feature.auth.signup.model.NickNameValidationState
import com.polzzak_android.presentation.feature.auth.signup.model.ProfileImageUiModel
import com.polzzak_android.presentation.feature.auth.signup.model.SignUpPage
import com.polzzak_android.presentation.feature.auth.signup.model.SignUpResultUiModel
import com.polzzak_android.presentation.feature.auth.signup.model.SignUpTermsOfServiceModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SignUpViewModel @AssistedInject constructor(
    private val signUpRepository: SignUpRepository,
    @Assisted private val userName: String?, @Assisted private val userType: SocialLoginType?
) : ViewModel() {
    private val _pageLiveData = MutableLiveData<SignUpPage>()
    val pageLiveData: LiveData<SignUpPage> = _pageLiveData

    private val _nickNameLiveData = MutableLiveData<NickNameUiModel>()
    val nickNameLiveData: LiveData<NickNameUiModel> = _nickNameLiveData

    private val _memberTypeLiveData = MutableLiveData<MemberTypeUiModel>()
    val memberTypeLiveData: LiveData<MemberTypeUiModel> = _memberTypeLiveData

    private val _profileImageLiveData = MutableLiveData<ProfileImageUiModel>(ProfileImageUiModel())
    val profileImageLiveData: LiveData<ProfileImageUiModel> = _profileImageLiveData

    private val _signUpResultLiveData =
        MutableLiveData<EventWrapper<ModelState<SignUpResultUiModel>>>()
    val signUpResultLiveData: LiveData<EventWrapper<ModelState<SignUpResultUiModel>>> =
        _signUpResultLiveData

    private val _termsOfServiceLiveData = MutableLiveData<SignUpTermsOfServiceModel>()
    val termsOfServiceLiveData: LiveData<SignUpTermsOfServiceModel> =
        _termsOfServiceLiveData

    private var checkNickNameValidationJob: Job? = null
    private var signUpJob: Job? = null

    init {
        _pageLiveData.value =
            safeLet(userName, userType) { _, _ -> SignUpPage.TERMS_OF_SERVICE } ?: SignUpPage.ERROR
    }

    fun moveNextPage() {
        when (pageLiveData.value) {
            SignUpPage.TERMS_OF_SERVICE -> _pageLiveData.value = SignUpPage.SELECT_TYPE
            SignUpPage.SELECT_TYPE -> _pageLiveData.value =
                if (memberTypeLiveData.value?.isKid() == true) SignUpPage.SET_NICKNAME else SignUpPage.SELECT_PARENT_TYPE

            SignUpPage.SELECT_PARENT_TYPE -> _pageLiveData.value = SignUpPage.SET_NICKNAME
            SignUpPage.SET_NICKNAME -> _pageLiveData.value = SignUpPage.SET_PROFILE_IMAGE
            else -> {
                //do nothing
            }
        }
    }

    fun movePrevPage() {
        when (pageLiveData.value) {
            SignUpPage.SELECT_TYPE -> _pageLiveData.value = SignUpPage.TERMS_OF_SERVICE
            SignUpPage.SELECT_PARENT_TYPE -> _pageLiveData.value = SignUpPage.SELECT_TYPE
            SignUpPage.SET_NICKNAME -> _pageLiveData.value =
                if (memberTypeLiveData.value?.isParent() == true) SignUpPage.SELECT_PARENT_TYPE else SignUpPage.SELECT_TYPE

            SignUpPage.SET_PROFILE_IMAGE -> _pageLiveData.value = SignUpPage.SET_NICKNAME
            else -> {
                //do nothing
            }
        }
    }

    fun selectTypeParent() {
        if (memberTypeLiveData.value?.isParent() == true) return
        _memberTypeLiveData.value = MemberTypeUiModel(selectedType = MemberTypeUiModel.Type.PARENT)
    }

    fun selectTypeKid() {
        if (memberTypeLiveData.value?.isKid() == true) return
        _memberTypeLiveData.value =
            MemberTypeUiModel(
                selectedType = MemberTypeUiModel.Type.KID,
                selectedTypeId = KID_TYPE_ID
            )
    }

    fun selectParentType(selectedTypeId: Int?) {
        val memberTypeUiModel = memberTypeLiveData.value ?: MemberTypeUiModel()
        _memberTypeLiveData.value = memberTypeUiModel.copy(selectedTypeId = selectedTypeId)
    }

    fun setNickNameValue(nickName: String) {
        checkNickNameValidationJob?.cancel()
        _nickNameLiveData.value = NickNameUiModel(nickName = nickName)
    }

    fun setProfileImagePath(path: String?) {
        _profileImageLiveData.value = ProfileImageUiModel(path = path)
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
                        val signUpResultUiModel =
                            SignUpResultUiModel(accessToken = it, memberTypeId = memberTypeId)
                        setSignUpResultSuccess(data = signUpResultUiModel)
                    } ?: run {
                        setSignUpResultError()
                    }
                }.onError { exception, _ ->
                    setSignUpResultError(exception = exception)
                }
            }
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

    private fun setSignUpResultSuccess(data: SignUpResultUiModel) {
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