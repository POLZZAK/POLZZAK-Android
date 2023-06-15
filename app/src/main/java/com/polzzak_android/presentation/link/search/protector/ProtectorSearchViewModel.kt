package com.polzzak_android.presentation.link.search.protector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.polzzak_android.data.repository.FamilyRepository
import com.polzzak_android.presentation.link.search.base.BaseSearchViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class ProtectorSearchViewModel @AssistedInject constructor(
    familyRepository: FamilyRepository,
    @Assisted initAccessToken: String
) : BaseSearchViewModel(familyRepository, initAccessToken) {

    @AssistedFactory
    interface ProtectorSearchViewModelAssistedFactory {
        fun create(initAccessToken: String): ProtectorSearchViewModel
    }

    companion object {
        fun provideFactory(
            protectorSearchViewModelAssistedFactory: ProtectorSearchViewModelAssistedFactory,
            initAccessToken: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return protectorSearchViewModelAssistedFactory.create(initAccessToken = initAccessToken) as T
            }
        }
    }
}