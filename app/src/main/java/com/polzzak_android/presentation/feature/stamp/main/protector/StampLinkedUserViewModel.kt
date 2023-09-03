package com.polzzak_android.presentation.feature.stamp.main.protector

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polzzak_android.data.remote.model.response.UserInfoDto
import com.polzzak_android.data.repository.FamilyRepository
import com.polzzak_android.presentation.common.model.ModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StampLinkedUserViewModel @Inject constructor(
    private val familyRepository: FamilyRepository
) : ViewModel() {

    // 연동된 유저 리스트
    private val _linkedUserList: MutableLiveData<ModelState<List<UserInfoDto>>> = MutableLiveData()
    val linkedUserList get() = _linkedUserList

    private var _hasLinkedUser: Boolean = false
    val hasLinkedUser get() = _hasLinkedUser


    /**
     * 연동된 유저 조회
     */
    fun requestLinkedUserList(accessToken: String) {
        viewModelScope.launch {
            _linkedUserList.value = ModelState.Loading()
            familyRepository.requestLinkedUsers(accessToken).onSuccess { data ->
                _hasLinkedUser = data?.families?.isNotEmpty() ?: false
                if (data?.families != null) {
                    _linkedUserList.value = ModelState.Success(data.families)
                }
            }.onError { exception, data ->
                _linkedUserList.value = ModelState.Error(exception)
            }
        }
    }

    fun getLinkedUserList() = _linkedUserList.value?.data
}