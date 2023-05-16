package com.polzzak_android.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polzzak_android.common.model.ApiError
import com.polzzak_android.common.model.ApiResult
import com.polzzak_android.common.model.SocialLoginType
import com.polzzak_android.common.model.UserInfo
import com.polzzak_android.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val loginRepository: LoginRepository) :
    ViewModel() {
    private val _userInfoLiveData = MutableLiveData<ApiResult<UserInfo>>()
    val userInfoLiveData: LiveData<ApiResult<UserInfo>> = _userInfoLiveData

    private var loginJob: Job? = null

    fun requestLogin(id: String, loginType: SocialLoginType) {
        if (loginJob?.isCompleted == false) return

        loginJob = viewModelScope.launch {
            _userInfoLiveData.value = ApiResult.Loading()
            delay(3000)
            loginRepository.requestUserInfo(id = id, loginType = loginType).onSuccess {
                _userInfoLiveData.value = ApiResult.Success(it)
            }.onFailure {
                _userInfoLiveData.value = ApiResult.Error(errorType = ApiError.NETWORK)
            }
        }
    }
}