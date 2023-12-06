package com.polzzak_android.presentation.component.newbottomsheet.makestamp

import androidx.fragment.app.viewModels
import com.polzzak_android.R
import com.polzzak_android.databinding.BottomsheetFragmentChoiceStampBinding
import com.polzzak_android.presentation.common.util.grandParentFragment
import com.polzzak_android.presentation.component.newbottomsheet.base.BaseSheetFragment
import com.polzzak_android.presentation.component.newbottomsheet.base.SheetEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChoiceStampSheet : BaseSheetFragment<BottomsheetFragmentChoiceStampBinding>() {
    override val layoutId: Int
        get() = R.layout.bottomsheet_fragment_choice_stamp

    private val viewModel: MakeStampViewModel by viewModels({ grandParentFragment })

    override fun initialize() {
        binding.apply {
            btnPrev.setOnClickListener {
                viewModel.emitSheetEvent(SheetEvent.PREV)
            }
        }
    }
}