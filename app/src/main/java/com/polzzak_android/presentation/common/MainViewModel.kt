package com.polzzak_android.presentation.common

import androidx.lifecycle.ViewModel
import com.polzzak_android.data.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
) : ViewModel() {

}