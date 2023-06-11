package com.polzzak_android.presentation.search.kid

import com.polzzak_android.data.repository.FamilyRepository
import com.polzzak_android.presentation.search.base.BaseSearchViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class KidSearchViewModel @Inject constructor(familyRepository: FamilyRepository) :
    BaseSearchViewModel(familyRepository) {
}