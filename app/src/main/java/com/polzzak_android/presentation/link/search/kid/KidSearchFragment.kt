package com.polzzak_android.presentation.link.search.kid

import androidx.fragment.app.viewModels
import com.polzzak_android.presentation.common.util.getAccessTokenOrNull
import com.polzzak_android.presentation.link.search.base.BaseSearchFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class KidSearchFragment : BaseSearchFragment() {
    //TODO string resource 적용
    override val targetString: String = "보호자"

    @Inject
    lateinit var kidSearchViewModelAssistedFactory: KidSearchViewModel.KidSearchViewModelAssistedFactory

    override val searchViewModel by viewModels<KidSearchViewModel> {
        KidSearchViewModel.provideFactory(
            kidSearchViewModelAssistedFactory = kidSearchViewModelAssistedFactory,
            initAccessToken = getAccessTokenOrNull() ?: ""
        )
    }
}