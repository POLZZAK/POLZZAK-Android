package com.polzzak_android.presentation.search.kid

import androidx.fragment.app.viewModels
import com.polzzak_android.presentation.search.base.BaseSearchDialogFragment

class KidSearchDialogFragment : BaseSearchDialogFragment() {
    //TODO string resource 적용
    override val typeString: String = "보호자"
    override val searchViewModel by viewModels<KidSearchViewModel>()
}