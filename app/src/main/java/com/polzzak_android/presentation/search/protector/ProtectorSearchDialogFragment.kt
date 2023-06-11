package com.polzzak_android.presentation.search.protector

import androidx.fragment.app.viewModels
import com.polzzak_android.presentation.search.base.BaseSearchDialogFragment

class ProtectorSearchDialogFragment : BaseSearchDialogFragment() {
    //TODO string resource 적용
    override val typeString: String = "아이"
    override val searchViewModel by viewModels<ProtectorSearchViewModel>()
}