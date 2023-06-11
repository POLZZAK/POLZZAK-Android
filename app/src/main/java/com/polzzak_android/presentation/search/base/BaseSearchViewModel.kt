package com.polzzak_android.presentation.search.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.polzzak_android.data.repository.FamilyRepository
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.search.model.SearchMainRequestModel
import com.polzzak_android.presentation.search.model.SearchPageTypeModel

abstract class BaseSearchViewModel(private val familyRepository: FamilyRepository) : ViewModel() {
    private val _pageLiveData = MutableLiveData<SearchPageTypeModel>(SearchPageTypeModel.MAIN)
    val pageLiveData: LiveData<SearchPageTypeModel> = _pageLiveData

    private val _searchQueryLiveData = MutableLiveData<String>("")
    val searchQueryLiveData: LiveData<String> = _searchQueryLiveData

    private val _requestLiveData = MutableLiveData<ModelState<List<SearchMainRequestModel>>>()
    val requestLiveData: LiveData<ModelState<List<SearchMainRequestModel>>> = _requestLiveData

    fun setPage(page: SearchPageTypeModel) {
        if (page == pageLiveData.value) return
        _pageLiveData.value = page
    }

    fun setSearchQuery(query: String) {
        _searchQueryLiveData.value = query
    }
}