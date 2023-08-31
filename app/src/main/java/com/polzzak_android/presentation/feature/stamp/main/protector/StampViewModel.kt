package com.polzzak_android.presentation.feature.stamp.main.protector

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polzzak_android.data.remote.model.response.toStampBoardModel
import com.polzzak_android.data.repository.FamilyRepository
import com.polzzak_android.data.repository.StampBoardRepository
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.feature.stamp.model.StampBoardModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StampViewModel @Inject constructor(
    private val stampBoardRepository: StampBoardRepository,
    private val familyRepository: FamilyRepository
) : ViewModel() {

    private val _hasLinkedUser: MutableLiveData<ModelState<Boolean>> = MutableLiveData()
    val hasLinkedUser get() = _hasLinkedUser

    private val _stampBoardList: MutableLiveData<ModelState<List<StampBoardModel>>> = MutableLiveData()
    val stampBoardList get() = _stampBoardList
    
    fun checkHasLinkedUser(accessToken: String) {
        viewModelScope.launch {
            _hasLinkedUser.value = ModelState.Loading()
            familyRepository.requestLinkedUsers(accessToken).onSuccess { data ->
                _hasLinkedUser.value = ModelState.Success(data?.families?.isNotEmpty() ?: false)
            }.onError { exception, data ->
                _hasLinkedUser.value = ModelState.Error(exception)
            }
        }
    }

    fun getStampBoardList(accessToken: String, linkedMemberId: String?, stampBoardGroup: String) {
        viewModelScope.launch {
            _stampBoardList.value = ModelState.Loading()
            stampBoardRepository.getStampBoardList(accessToken, linkedMemberId, stampBoardGroup).onSuccess { data ->
                val stampBoardList = data?.map {
                    it.toStampBoardModel()
                }
                _stampBoardList.value = ModelState.Success(stampBoardList!!)
            }.onError { exception, data ->
                _stampBoardList.value = ModelState.Error(exception)
            }
        }

    }
}