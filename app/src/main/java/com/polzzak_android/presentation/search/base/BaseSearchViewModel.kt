package com.polzzak_android.presentation.search.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.search.model.SearchPageTypeModel
import com.polzzak_android.presentation.search.model.SearchUserModel

abstract class BaseSearchViewModel : ViewModel() {
    private val _pageLiveData = MutableLiveData<SearchPageTypeModel>(SearchPageTypeModel.MAIN)
    val pageLiveData: LiveData<SearchPageTypeModel> = _pageLiveData

    private val _searchQueryLiveData = MutableLiveData<String>("")
    val searchQueryLiveData: LiveData<String> = _searchQueryLiveData

    private val _requestLiveData = MutableLiveData<ModelState<List<SearchUserModel>>>()
    val requestLiveData: LiveData<ModelState<List<SearchUserModel>>> = _requestLiveData

    fun setPage(page: SearchPageTypeModel) {
        if (page == pageLiveData.value) return
        _pageLiveData.value = page
    }

    fun setSearchQuery(query: String) {
        _searchQueryLiveData.value = query
    }
}