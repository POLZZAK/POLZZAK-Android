package com.polzzak_android.temp.hiltsample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HiltSampleViewModel @Inject constructor(private val repository: HiltSampleRepository) :
    ViewModel() {
    private val _sampleLiveData = MutableLiveData<Int>()
    val sampleLiveData: LiveData<Int> = _sampleLiveData

    fun fetchSampleData() = viewModelScope.launch {
        _sampleLiveData.value = repository.fetchSampleCount()
    }
}