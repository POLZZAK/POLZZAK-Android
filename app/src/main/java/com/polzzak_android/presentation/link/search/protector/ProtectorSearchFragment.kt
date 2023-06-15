package com.polzzak_android.presentation.link.search.protector

import androidx.fragment.app.viewModels
import com.polzzak_android.presentation.common.util.getAccessTokenOrNull
import com.polzzak_android.presentation.link.search.base.BaseSearchFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProtectorSearchFragment : BaseSearchFragment() {
    //TODO string resource 적용
    override val targetString: String = "아이"

    @Inject
    lateinit var protectorSearchViewModelAssistedFactory: ProtectorSearchViewModel.ProtectorSearchViewModelAssistedFactory

    override val searchViewModel by viewModels<ProtectorSearchViewModel> {
        ProtectorSearchViewModel.provideFactory(
            protectorSearchViewModelAssistedFactory = protectorSearchViewModelAssistedFactory,
            initAccessToken = getAccessTokenOrNull() ?: ""
        )
    }
}