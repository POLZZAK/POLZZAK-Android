package com.polzzak_android.presentation.link.management

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.polzzak_android.presentation.link.management.model.LinkManagementHomeTabTypeModel

class LinkManagementViewModel : ViewModel() {
    private val _homeTabTypeLiveData = MutableLiveData(LinkManagementHomeTabTypeModel.LINK)
    val homeTabTypeLiveData: LiveData<LinkManagementHomeTabTypeModel> = _homeTabTypeLiveData

    fun setHomeTabType(tabType: LinkManagementHomeTabTypeModel) {
        if (tabType == homeTabTypeLiveData.value) return
        _homeTabTypeLiveData.value = tabType
    }
}