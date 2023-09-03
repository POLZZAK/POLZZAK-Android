package com.polzzak_android.presentation.feature.myPage.notice

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentMyNoticeBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.item.FullLoadingItem
import com.polzzak_android.presentation.common.item.LoadMoreLoadingSpinnerItem
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.common.util.BindableItemAdapter
import com.polzzak_android.presentation.common.util.toPx
import com.polzzak_android.presentation.component.toolbar.ToolbarData
import com.polzzak_android.presentation.component.toolbar.ToolbarHelper
import com.polzzak_android.presentation.feature.myPage.notice.item.MyNoticeEmptyItem
import com.polzzak_android.presentation.feature.myPage.notice.item.MyNoticeItem
import com.polzzak_android.presentation.feature.myPage.notice.model.MyNoticeModel
import com.polzzak_android.presentation.feature.myPage.notice.model.MyNoticesModel

//TODO 바텀네비 invisible
class MyNoticeFragment : BaseFragment<FragmentMyNoticeBinding>(), MyNoticeClickListener {
    override val layoutResId: Int = R.layout.fragment_my_notice

    private val noticeViewModel by viewModels<MyNoticeViewModel>()

    override fun initView() {
        super.initView()
        initToolbar()
        initRecyclerView()
    }

    private fun initToolbar() {
        with(binding) {
            ToolbarHelper(
                data = ToolbarData(
                    popStack = findNavController(),
                    titleText = getString(R.string.common_notice)
                ),
                toolbar = inToolbar
            ).set()
        }
    }

    private fun initRecyclerView() {
        val context = binding.root.context ?: return
        with(binding.rvNotices) {
            layoutManager = LinearLayoutManager(context)
            adapter = BindableItemAdapter()
            val marginPx = ITEM_MARGIN_DP.toPx(context)
            addItemDecoration(MyNoticeItemDecoration(marginPx = marginPx))
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1) && noticeViewModel.noticesLiveData.value is ModelState.Success) noticeViewModel.requestMoreNotices()
                }
            })
        }
    }

    override fun initObserver() {
        super.initObserver()
        noticeViewModel.noticesLiveData.observe(viewLifecycleOwner) {
            val adapter = (binding.rvNotices.adapter as? BindableItemAdapter) ?: return@observe
            val noticeItem = createNoticeItem(it.data ?: MyNoticesModel())
            val items = mutableListOf<BindableItem<*>>()
            when (it) {
                is ModelState.Loading -> {
                    if (it.data?.notices.isNullOrEmpty()) items.add(FullLoadingItem())
                    else items.addAll(noticeItem + LoadMoreLoadingSpinnerItem())
                }

                is ModelState.Success -> items.addAll(noticeItem)
                is ModelState.Error -> {
                    //TODO error handling
                }
            }
            adapter.updateItem(item = items)
        }
    }

    private fun createNoticeItem(model: MyNoticesModel) = model.notices.map {
        MyNoticeItem(model = it, clickListener = this)
    }.ifEmpty { listOf(MyNoticeEmptyItem()) }

    override fun onClickNotice(model: MyNoticeModel) {
        val bundle = Bundle().apply {
            putParcelable(MyNoticeDetailFragment.ARGUMENT_NOTICE_KEY, model)
        }
        findNavController().navigate(R.id.action_myNoticeFragment_to_myNoticeDetailFragment, bundle)
    }

    companion object {
        private const val ITEM_MARGIN_DP = 16
    }

}