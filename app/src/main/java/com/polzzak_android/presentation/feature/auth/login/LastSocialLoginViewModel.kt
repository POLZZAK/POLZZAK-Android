package com.polzzak_android.presentation.feature.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polzzak_android.data.repository.LastSocialLoginRepository
import com.polzzak_android.presentation.feature.auth.model.SocialLoginType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LastSocialLoginViewModel @Inject constructor(
    private val lastSocialLoginRepository: LastSocialLoginRepository
) : ViewModel() {
    private val _lastSocialLoginTypeLiveData = MutableLiveData<SocialLoginType?>()
    val lastSocialLoginTypeLiveData: LiveData<SocialLoginType?> = _lastSocialLoginTypeLiveData
    private var lastSocialLoginTypeJob: Job? = null

    fun loadLastSocialLoginType() {
        if (lastSocialLoginTypeJob?.isCompleted == false) return
        lastSocialLoginTypeJob = viewModelScope.launch {
            _lastSocialLoginTypeLiveData.value = lastSocialLoginRepository.loadLastSocialLoginType()
        }
    }

    fun saveLastSocialLoginType(socialLoginType: SocialLoginType) {
        if (lastSocialLoginTypeJob?.isCompleted == false) return
        lastSocialLoginTypeJob = viewModelScope.launch {
            lastSocialLoginRepository.saveLastLogin(socialLoginType)
        }
    }
}