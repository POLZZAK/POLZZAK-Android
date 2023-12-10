package com.polzzak_android.presentation.component.newbottomsheet.makestamp

import android.graphics.Rect
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.polzzak_android.R
import com.polzzak_android.databinding.BottomsheetFragmentChoiceStampBinding
import com.polzzak_android.presentation.common.util.grandParentFragment
import com.polzzak_android.presentation.common.util.toPx
import com.polzzak_android.presentation.component.newbottomsheet.base.BaseSheetFragment
import com.polzzak_android.presentation.component.newbottomsheet.base.SheetEvent
import com.polzzak_android.presentation.feature.stamp.model.StampIcon
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ChoiceStampSheet : BaseSheetFragment<BottomsheetFragmentChoiceStampBinding>() {
    override val layoutId: Int
        get() = R.layout.bottomsheet_fragment_choice_stamp

    private val adapter: StampListAdapter by lazy {
        StampListAdapter(viewModel::setStampDesignId)
    }

    private val viewModel: MakeStampViewModel by viewModels({ grandParentFragment })

    override fun initialize() {
        binding.apply {
            btnPrev.setOnClickListener {
                viewModel.emitSheetEvent(SheetEvent.PREV)
            }

            rvStampList.adapter = adapter
            rvStampList.addItemDecoration(object : ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.apply {
                        left = 4.toPx(view.context)
                        right = 4.toPx(view.context)
                    }
                }
            })
        }

        observe()
    }

    private fun observe() = lifecycleScope.launch {
        viewModel.selectedStampDesignId.collectLatest {
            val stampIcon = StampIcon.values()[it]
            binding.ivSelectedStamp.setImageResource(stampIcon.resId)
            binding.tvStampName.text = stampIcon.title
        }
    }
}