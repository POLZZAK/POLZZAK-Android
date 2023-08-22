package com.polzzak_android.presentation.feature.notification.base

import android.util.DisplayMetrics
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentNotificationBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.item.LoadMoreLoadingSpinnerItem
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.common.util.BindableItemAdapter
import com.polzzak_android.presentation.common.util.toPx
import com.polzzak_android.presentation.feature.notification.NotificationItemDecoration
import com.polzzak_android.presentation.feature.notification.NotificationViewModel
import com.polzzak_android.presentation.feature.notification.item.NotificationEmptyItem
import com.polzzak_android.presentation.feature.notification.item.NotificationItem
import com.polzzak_android.presentation.feature.notification.item.NotificationRefreshItem
import com.polzzak_android.presentation.feature.notification.item.NotificationSkeletonLoadingItem
import com.polzzak_android.presentation.feature.notification.model.NotificationModel
import com.polzzak_android.presentation.feature.notification.model.NotificationRefreshStatusType


abstract class BaseNotificationFragment : BaseFragment<FragmentNotificationBinding>() {
    override val layoutResId: Int = R.layout.fragment_notification
    private val notificationViewModel by viewModels<NotificationViewModel>()
    private val smoothScroller by lazy {
        object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }

            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
                return super.calculateSpeedPerPixel(displayMetrics) * 4f
            }
        }
    }

    override fun initView() {
        super.initView()
        initRecyclerView()
        binding.ivBtnSetting.setOnClickListener {
            //TODO move to setting page
        }
    }

    private fun initRecyclerView() {
        with(binding.rvNotifications) {
            val layoutManager = LinearLayoutManager(context)
            this.layoutManager = layoutManager
            val paddingPx = NOTIFICATIONS_PADDING_DP.toPx(context)
            val betweenMarginPx = NOTIFICATIONS_BETWEEN_MARGIN_DP.toPx(context)
            val itemDecoration = NotificationItemDecoration(
                paddingPx = paddingPx,
                betweenMarginPx = betweenMarginPx
            )
            addItemDecoration(itemDecoration)
            adapter = BindableItemAdapter()
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1)) notificationViewModel.requestMoreNotifications()
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    when (notificationViewModel.notificationLiveData.value?.data?.refreshStatusType) {
                        NotificationRefreshStatusType.Normal -> onScrollStateChangedRefreshNormal(
                            recyclerView = recyclerView,
                            newState = newState
                        )

                        else -> {
                            //do nothing
                        }
                    }
                }
            })
        }
    }

    private fun onScrollStateChangedRefreshNormal(recyclerView: RecyclerView, newState: Int) {
        val layoutManager = (recyclerView.layoutManager as? LinearLayoutManager) ?: return
        val firstCompletelyVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
        if (firstCompletelyVisibleItem == 0) {
            notificationViewModel.refreshNotifications()
            return
        }
        val firstVisibleItem =
            layoutManager.findFirstVisibleItemPosition().takeIf { it >= 0 } ?: return
        if (firstVisibleItem < 1 && newState != RecyclerView.SCROLL_STATE_DRAGGING) {
            smoothScroller.targetPosition = 1
            layoutManager.startSmoothScroll(smoothScroller)
        }
    }

    override fun initObserver() {
        super.initObserver()
        initNotificationObserver()
    }

    private fun initNotificationObserver() {
        notificationViewModel.notificationLiveData.observe(viewLifecycleOwner) {
            val layoutManager =
                (binding.rvNotifications.layoutManager as? LinearLayoutManager) ?: return@observe
            val adapter =
                (binding.rvNotifications.adapter as? BindableItemAdapter) ?: return@observe
            val refreshStatusType =
                it.data?.refreshStatusType ?: NotificationRefreshStatusType.Disable
            val items =
                mutableListOf<BindableItem<*>>(NotificationRefreshItem(statusType = refreshStatusType))
            var updateCallback: (() -> Unit)? = null
            when (it) {
                is ModelState.Loading -> {
                    if (it.data?.items == null) {
                        items.addAll(createSkeletonLoadingItems())
                    } else {
                        items.addAll(createNotificationItems(data = it.data?.items))
                        if (!notificationViewModel.isRefreshed) items.add(
                            LoadMoreLoadingSpinnerItem(
                                marginTopDp = 8
                            )
                        )
                    }
                }

                is ModelState.Success -> {
                    items.addAll(createNotificationItems(data = it.data.items))
                    if (notificationViewModel.isRefreshed) {
                        updateCallback = {
                            layoutManager.scrollToPositionWithOffset(1, 0)
                        }
                    }
                }

                is ModelState.Error -> {
                    //TODO 에러처리
                }
            }

            adapter.updateItem(item = items, commitCallback = updateCallback)
        }
    }

    private fun createSkeletonLoadingItems() = List(LOADING_SKELETON_ITEM_COUNT) {
        NotificationSkeletonLoadingItem()
    }

    private fun createNotificationItems(data: List<NotificationModel>?): List<BindableItem<*>> {
        return if (data.isNullOrEmpty()) listOf(NotificationEmptyItem()) else data.map {
            NotificationItem(
                model = it,
                itemStateController = notificationViewModel
            )
        }
    }

    companion object {
        private const val LOADING_SKELETON_ITEM_COUNT = 5
        private const val NOTIFICATIONS_PADDING_DP = 16
        private const val NOTIFICATIONS_BETWEEN_MARGIN_DP = 8
    }
}