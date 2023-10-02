package com.polzzak_android.presentation.feature.myPage.profile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polzzak_android.data.remote.model.response.ProfileDto
import com.polzzak_android.data.remote.model.response.UserInfoDto
import com.polzzak_android.data.repository.UserRepository
import com.polzzak_android.presentation.common.model.ModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    // 사용자 프로필 정보
    private val _userProfile: MutableLiveData<ModelState<ProfileDto>> = MutableLiveData()
    val userProfile get() = _userProfile

    // 사용자 프로필 업데이트 여부
    private val _profileUpdateSuccess: MutableLiveData<ModelState<String?>> = MutableLiveData()
    val profileUpdateSuccess get() = _profileUpdateSuccess

    // 프로필 이미지
    private var _profileImg: String = ""

    // 프로필 닉네임
    private var _profileNickname: String = ""

    fun getUserProfile(accessToken: String) {
        viewModelScope.launch {
            _userProfile.value = ModelState.Loading()
            userRepository.requestProfile(accessToken).onSuccess { data ->
                if (data != null) {
                    _userProfile.value = ModelState.Success(data)
                    return@launch
                }
            }.onError { exception, _ ->
                _userProfile.value = ModelState.Error(exception)
            }
        }
    }

    fun getUserNickname(): String? = _userProfile.value?.data?.nickName

    fun setUserNickname(nickname: String) {
        _profileNickname = nickname
    }

    fun setProfileImg(uri: String) {
        _profileImg = uri
    }

    fun updateUserProfile(accessToken: String) {
        viewModelScope.launch {
            val profileInfo = _userProfile.value?.data?.copy(
                nickName = _profileNickname
            )
            _profileUpdateSuccess.value = ModelState.Loading()
            userRepository.updateUserInfo(accessToken = accessToken, profileInfo = profileInfo!!, profileImagePath = _profileImg).onSuccess {
                updateUserNickname(accessToken)
            }.onError { exception, _ ->
                _profileUpdateSuccess.value = ModelState.Error(exception)
            }
        }
    }

    private fun updateUserNickname(accessToken: String) {
        viewModelScope.launch {
            userRepository.updateUserNickname(accessToken, _profileNickname).onSuccess {
                _profileUpdateSuccess.value = ModelState.Success(null)
            }.onError { exception, unit ->
                _profileUpdateSuccess.value = ModelState.Error(exception)
            }
        }
    }
}