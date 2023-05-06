package com.polzzak_android.presentation.login

import androidx.lifecycle.ViewModel
import com.polzzak_android.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: LoginRepository) : ViewModel() {

}