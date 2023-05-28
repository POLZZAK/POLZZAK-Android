package com.polzzak_android.presentation.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.polzzak_android.common.model.MemberType
import com.polzzak_android.common.model.SocialLoginType
import com.polzzak_android.common.util.isError
import com.polzzak_android.common.util.isSuccess
import com.polzzak_android.common.util.safeLet
import com.polzzak_android.common.util.toApiResult
import com.polzzak_android.data.repository.SignUpRepository
import com.polzzak_android.presentation.signup.model.MemberTypeUiModel
import com.polzzak_android.presentation.signup.model.NickNameUiModel
import com.polzzak_android.presentation.signup.model.NickNameValidationState
import com.polzzak_android.presentation.signup.model.SignUpPage
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

    //TODO 서버 전송 타입으로 변경(현재는 임시타입)
    private var profile: ByteArray? = null

    private var checkNickNameValidationJob: Job? = null

    init {
        _pageLiveData.value =
            safeLet(userName, userType) { _, _ -> SignUpPage.SELECT_TYPE } ?: SignUpPage.ERROR
    }

    fun moveNextPage() {
        when (pageLiveData.value) {
            SignUpPage.SELECT_TYPE -> _pageLiveData.value =
                if (memberTypeLiveData.value?.type is MemberType.Kid) SignUpPage.SET_NICKNAME else SignUpPage.SELECT_PARENT_TYPE

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
                clearParentType()
                _pageLiveData.value = SignUpPage.SELECT_TYPE
            }

            SignUpPage.SET_NICKNAME -> {
                clearNickName()
                _pageLiveData.value =
                    if (memberTypeLiveData.value?.isParentType == true) SignUpPage.SELECT_PARENT_TYPE else SignUpPage.SELECT_TYPE
            }

            SignUpPage.SET_PROFILE_IMAGE -> {
                clearProfileImage()
                _pageLiveData.value = SignUpPage.SET_NICKNAME
            }

            else -> {
                //do nothing
            }
        }
    }

    fun selectTypeParent() {
        if (memberTypeLiveData.value?.isParentType == true) return
        _memberTypeLiveData.value = MemberTypeUiModel(type = null, isParentType = true)
    }

    fun selectTypeKid() {
        if (memberTypeLiveData.value?.type is MemberType.Kid) return
        _memberTypeLiveData.value = MemberTypeUiModel(type = MemberType.Kid(), isParentType = false)
    }

    fun selectParentType(parentType: MemberType.Parent?) {
        val memberTypeUiModel = memberTypeLiveData.value ?: MemberTypeUiModel()
        _memberTypeLiveData.value = memberTypeUiModel.copy(type = parentType)
    }

    fun setNickNameValue(nickName: String) {
        checkNickNameValidationJob?.cancel()
        _nickNameLiveData.value = NickNameUiModel(nickName = nickName)
    }

    private fun clearProfileImage() {
        profile = null
    }

    private fun clearParentType() {
        _memberTypeLiveData.value =
            memberTypeLiveData.value?.run { copy(type = null) } ?: MemberTypeUiModel()
    }

    private fun clearNickName() {
        checkNickNameValidationJob?.cancel()
        _nickNameLiveData.value = NickNameUiModel()
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