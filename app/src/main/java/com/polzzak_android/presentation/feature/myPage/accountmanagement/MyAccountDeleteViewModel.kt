package com.polzzak_android.presentation.feature.myPage.accountmanagement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.feature.myPage.accountmanagement.model.MyAccountDeleteMenuModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyAccountDeleteViewModel : ViewModel() {
    private val _myAccountMenuLiveData = MutableLiveData<MyAccountDeleteMenuModel>()
    val myAccountMenuLiveData: LiveData<MyAccountDeleteMenuModel> = _myAccountMenuLiveData

    private val _deleteAccountLiveData = MutableLiveData<ModelState<Unit>>()
    val deleteAccountLiveData: LiveData<ModelState<Unit>> = _deleteAccountLiveData
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

    fun deleteAccount() {
        if (deleteAccountJob?.isCompleted == false) return
        deleteAccountJob = viewModelScope.launch {
            //TODO 회원탈퇴 API 호출
            _deleteAccountLiveData.value = ModelState.Loading()
            delay(2000)
            //onSuccess
            _deleteAccountLiveData.value = ModelState.Success(Unit)
        }

    }
}