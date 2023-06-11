package com.polzzak_android.presentation.search.protector

import com.polzzak_android.data.repository.FamilyRepository
import com.polzzak_android.presentation.search.base.BaseSearchViewModel
import javax.inject.Inject

class ProtectorSearchViewModel @Inject constructor(familyRepository: FamilyRepository) :
    BaseSearchViewModel(familyRepository) {
}