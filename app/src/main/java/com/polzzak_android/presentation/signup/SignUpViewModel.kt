package com.polzzak_android.presentation.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.polzzak_android.common.model.MemberType
import com.polzzak_android.common.model.SocialLoginType
import com.polzzak_android.common.util.safeLet
import com.polzzak_android.presentation.signup.model.MemberTypeUiModel
import com.polzzak_android.presentation.signup.model.NickNameUiModel
import com.polzzak_android.presentation.signup.model.SignUpPage
import kotlinx.coroutines.Job

class SignUpViewModel(private val userName: String?, private val userType: SocialLoginType?) :
    ViewModel() {
    private val _pageLiveData = MutableLiveData<SignUpPage>()
    val pageLiveData: LiveData<SignUpPage> = _pageLiveData

    private val _nickNameLiveData = MutableLiveData<NickNameUiModel>()
    val nickNameLiveData: LiveData<NickNameUiModel> = _nickNameLiveData

    private val _memberTypeLiveData = MutableLiveData<MemberTypeUiModel>()
    val memberTypeLiveData: LiveData<MemberTypeUiModel> = _memberTypeLiveData

    //TODO 서버 전송 타입으로 변경(현재는 임시타입)
    private var profile: ByteArray? = null

    private var checkNickNameJob: Job? = null

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
        _nickNameLiveData.value = NickNameUiModel()
    }

    fun checkIsDuplicatedNickName() {
        if (checkNickNameJob?.isCompleted == false) return
        //TODO 닉네임 중복체크 api
    }

    fun cancelCheckNickNameJob() {
        checkNickNameJob?.cancel()
    }

    companion object {
        class Factory(private val userName: String?, private val userType: SocialLoginType?) :
            ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SignUpViewModel(userName = userName, userType = userType) as T
            }
        }
    }
}