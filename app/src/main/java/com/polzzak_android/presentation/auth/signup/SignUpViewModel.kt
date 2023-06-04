package com.polzzak_android.presentation.auth.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.polzzak_android.common.util.livedata.EventWrapper
import com.polzzak_android.common.util.safeLet
import com.polzzak_android.data.repository.SignUpRepository
import com.polzzak_android.presentation.auth.model.SocialLoginType
import com.polzzak_android.presentation.auth.signup.model.MemberTypeUiModel
import com.polzzak_android.presentation.auth.signup.model.NickNameUiModel
import com.polzzak_android.presentation.auth.signup.model.NickNameValidationState
import com.polzzak_android.presentation.auth.signup.model.ProfileImageUiModel
import com.polzzak_android.presentation.auth.signup.model.SignUpPage
import com.polzzak_android.presentation.auth.signup.model.SignUpResultUiModel
import com.polzzak_android.presentation.common.model.ApiResult
import com.polzzak_android.presentation.common.model.MemberType.Companion.KID_TYPE_ID
import com.polzzak_android.presentation.common.util.isError
import com.polzzak_android.presentation.common.util.isSuccess
import com.polzzak_android.presentation.common.util.toApiResult
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

    private val _profileImageLiveData = MutableLiveData<ProfileImageUiModel>()
    val profileImageLiveData: LiveData<ProfileImageUiModel> = _profileImageLiveData

    private val _signUpResultLiveData =
        MutableLiveData<EventWrapper<ApiResult<SignUpResultUiModel>>>()
    val signUpResultLiveData: LiveData<EventWrapper<ApiResult<SignUpResultUiModel>>> =
        _signUpResultLiveData

    private var checkNickNameValidationJob: Job? = null
    private var signUpJob: Job? = null

    init {
        _pageLiveData.value =
            safeLet(userName, userType) { _, _ -> SignUpPage.SELECT_TYPE } ?: SignUpPage.ERROR
    }

    fun moveNextPage() {
        when (pageLiveData.value) {
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
            SignUpPage.SELECT_PARENT_TYPE -> {
                _pageLiveData.value = SignUpPage.SELECT_TYPE
            }

            SignUpPage.SET_NICKNAME -> {
                _pageLiveData.value =
                    if (memberTypeLiveData.value?.isParent() == true) SignUpPage.SELECT_PARENT_TYPE else SignUpPage.SELECT_TYPE
            }

            SignUpPage.SET_PROFILE_IMAGE -> {
                _pageLiveData.value = SignUpPage.SET_NICKNAME
            }

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
            val result = signUpRepository.requestCheckNickNameValidation(nickName = nickName)
                .toApiResult { null }
            if (result.isSuccess()) {
                _nickNameLiveData.value =
                    nickNameUiModel.copy(nickNameState = NickNameValidationState.VALID)
            } else if (result.isError()) {
                _nickNameLiveData.value =
                    nickNameUiModel.copy(nickNameState = NickNameValidationState.INVALID)
            }
        }
    }

    fun requestSignUp() {
        if (signUpJob?.isCompleted == false) return
        signUpJob = viewModelScope.launch {
            _signUpResultLiveData.value = EventWrapper(ApiResult.loading())
            safeLet(
                userName,
                memberTypeLiveData.value?.selectedTypeId,
                userType,
                nickNameLiveData.value?.nickName
            ) { userName, memberTypeId, userType, nickName ->
                val result = signUpRepository.requestSignUp(
                    userName = userName,
                    memberTypeId = memberTypeId,
                    socialType = userType,
                    nickName = nickName,
                    profileImagePath = profileImageLiveData.value?.path
                )
            }

        }
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