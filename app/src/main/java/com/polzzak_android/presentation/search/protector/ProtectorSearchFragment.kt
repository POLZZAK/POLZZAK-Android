package com.polzzak_android.presentation.search.protector

import androidx.fragment.app.viewModels
import com.polzzak_android.presentation.common.util.getAccessTokenOrNull
import com.polzzak_android.presentation.search.base.BaseSearchFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProtectorSearchFragment : BaseSearchFragment() {
    //TODO string resource 적용
    override val typeString: String = "아이"

    @Inject
    lateinit var protectorSearchViewModelAssistedFactory: ProtectorSearchViewModel.ProtectorSearchViewModelAssistedFactory

    override val searchViewModel by viewModels<ProtectorSearchViewModel> {
        ProtectorSearchViewModel.provideFactory(
            protectorSearchViewModelAssistedFactory = protectorSearchViewModelAssistedFactory,
            initAccessToken = getAccessTokenOrNull() ?: ""
        )
    }
}