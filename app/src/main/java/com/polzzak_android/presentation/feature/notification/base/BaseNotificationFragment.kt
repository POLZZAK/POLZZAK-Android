package com.polzzak_android.presentation.feature.notification.base

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentNotificationBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.common.util.BindableItemAdapter
import com.polzzak_android.presentation.common.util.toPx
import com.polzzak_android.presentation.feature.notification.NotificationItemDecoration
import com.polzzak_android.presentation.feature.notification.NotificationViewModel
import com.polzzak_android.presentation.feature.notification.item.NotificationItem
import com.polzzak_android.presentation.feature.notification.item.NotificationSkeletonLoadingItem
import com.polzzak_android.presentation.feature.notification.model.NotificationModel

abstract class BaseNotificationFragment : BaseFragment<FragmentNotificationBinding>() {
    override val layoutResId: Int = R.layout.fragment_notification
    private val notificationViewModel by viewModels<NotificationViewModel>()

    override fun initView() {
        super.initView()
        initRecyclerView()
        //TODO refresh
        binding.ivBtnSetting.setOnClickListener {
            //TODO move to setting page
        }
    }

    private fun initRecyclerView() {
        with(binding.rvNotifications) {
            layoutManager = LinearLayoutManager(context)
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
                    //TODO 무한스크롤
                }
            })
        }
    }

    override fun initObserver() {
        super.initObserver()
        notificationViewModel.notificationLiveData.observe(viewLifecycleOwner) {
            val items = mutableListOf<BindableItem<*>>()
            val adapter =
                (binding.rvNotifications.adapter as? BindableItemAdapter) ?: return@observe
            when (it) {
                is ModelState.Loading -> {
                    if (it.data?.items.isNullOrEmpty()) items.addAll(createSkeletonLoadingItems())
                    else items.addAll(createNotificationItems(data = it.data?.items ?: emptyList()))
                }

                is ModelState.Success -> {
                    //TODO 비어있는경우 대응
                    items.addAll(createNotificationItems(data = it.data.items))
                }

                is ModelState.Error -> {

                }
            }
            adapter.updateItem(item = items)
        }
    }

    private fun createSkeletonLoadingItems() = List(LOADING_SKELETON_ITEM_COUNT) {
        NotificationSkeletonLoadingItem()
    }

    private fun createNotificationItems(data: List<NotificationModel>): List<NotificationItem> {
        return data.map { NotificationItem(model = it) }
    }

    companion object {
        private const val LOADING_SKELETON_ITEM_COUNT = 5
        private const val NOTIFICATIONS_PADDING_DP = 16
        private const val NOTIFICATIONS_BETWEEN_MARGIN_DP = 8
    }
}