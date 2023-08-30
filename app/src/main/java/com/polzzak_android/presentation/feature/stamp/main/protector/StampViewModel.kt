package com.polzzak_android.presentation.feature.stamp.main.protector

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polzzak_android.data.remote.model.ApiException
import com.polzzak_android.data.remote.model.response.toStampBoardModel
import com.polzzak_android.data.repository.StampBoardRepository
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.feature.auth.signup.model.NickNameValidationState
import com.polzzak_android.presentation.feature.stamp.model.StampBoardModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StampViewModel @Inject constructor(
    private val repository: StampBoardRepository
) : ViewModel() {

    private val _stampBoardList: MutableLiveData<ModelState<StampBoardModel>> = MutableLiveData()
    val stampBoardList get() = _stampBoardList

    fun getStampBoardList(accessToken: String, linkedMemberId: String?, stampBoardGroup: String) {
        viewModelScope.launch {
            repository.getStampBoardList(accessToken, linkedMemberId, stampBoardGroup).onSuccess { data ->
                val stampBoardList = data?.data?.toStampBoardModel()
                _stampBoardList.value = ModelState.Success(stampBoardList!!)
            }.onError { exception, data ->
                _stampBoardList.value = ModelState.Error(exception)
            }
        }

    }
}