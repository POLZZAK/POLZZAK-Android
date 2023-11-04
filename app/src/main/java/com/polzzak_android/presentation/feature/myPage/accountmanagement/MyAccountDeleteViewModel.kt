package com.polzzak_android.presentation.feature.myPage.accountmanagement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polzzak_android.common.util.livedata.EventWrapper
import com.polzzak_android.data.repository.UserRepository
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.feature.myPage.accountmanagement.model.MyAccountDeleteMenuModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyAccountDeleteViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _myAccountMenuLiveData = MutableLiveData<MyAccountDeleteMenuModel>()
    val myAccountMenuLiveData: LiveData<MyAccountDeleteMenuModel> = _myAccountMenuLiveData

    private val _deleteAccountLiveData = MutableLiveData<ModelState<Unit>>()
    val deleteAccountLiveData: LiveData<ModelState<Unit>> = _deleteAccountLiveData
    private val _errorEventLiveData = MutableLiveData<EventWrapper<Exception>>()
    val errorEventLiveData: LiveData<EventWrapper<Exception>> = _errorEventLiveData
    private var deleteAccountJob: Job? = null

    fun toggleDeleteSocialAccountData() {
        _myAccountMenuLiveData.value =
            (myAccountMenuLiveData.value ?: MyAccountDeleteMenuModel()).run {
                copy(isCheckedDeleteSocialAccountData = !isCheckedDeleteSocialAccountData)
            }
    }

    fun toggleDeleteLink() {
        _myAccountMenuLiveData.value =
            (myAccountMenuLiveData.value ?: MyAccountDeleteMenuModel()).run {
                copy(isCheckedDeleteLink = !isCheckedDeleteLink)
            }
    }

    fun toggleDeletePoint() {
        _myAccountMenuLiveData.value =
            (myAccountMenuLiveData.value ?: MyAccountDeleteMenuModel()).run {
                copy(isCheckedDeletePoint = !isCheckedDeletePoint)
            }
    }

    fun toggleDeleteStampAndCoupon() {
        _myAccountMenuLiveData.value =
            (myAccountMenuLiveData.value ?: MyAccountDeleteMenuModel()).run {
                copy(isCheckedDeleteStampAndCoupon = !isCheckedDeleteStampAndCoupon)
            }
    }

    fun deleteAccount(accessToken: String) {
        if (deleteAccountJob?.isCompleted == false) return
        deleteAccountJob = viewModelScope.launch {
            userRepository.deleteUser(accessToken = accessToken).onSuccess {
                _deleteAccountLiveData.value = ModelState.Success(Unit)
            }.onError { exception, _ ->
                _deleteAccountLiveData.value = ModelState.Error(exception)
                _errorEventLiveData.value = EventWrapper(exception)
            }
        }
    }
}