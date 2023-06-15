package com.polzzak_android.presentation.link.search.kid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.polzzak_android.data.repository.FamilyRepository
import com.polzzak_android.presentation.link.search.base.BaseSearchViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class KidSearchViewModel @AssistedInject constructor(
    familyRepository: FamilyRepository,
    @Assisted initAccessToken: String
) : BaseSearchViewModel(familyRepository, initAccessToken) {
    @AssistedFactory
    interface KidSearchViewModelAssistedFactory {
        fun create(initAccessToken: String): KidSearchViewModel
    }

    companion object {
        fun provideFactory(
            kidSearchViewModelAssistedFactory: KidSearchViewModelAssistedFactory,
            initAccessToken: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return kidSearchViewModelAssistedFactory.create(initAccessToken = initAccessToken) as T
            }
        }
    }
}