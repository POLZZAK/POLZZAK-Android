package com.polzzak_android.presentation.feature.notification.list.base

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.polzzak_android.R
import com.polzzak_android.common.util.livedata.EventWrapperObserver
import com.polzzak_android.data.remote.model.isAccessTokenException
import com.polzzak_android.databinding.FragmentNotificationListBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.item.LoadMoreLoadingSpinnerItem
import com.polzzak_android.presentation.common.model.MemberType
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.common.util.BindableItemAdapter
import com.polzzak_android.presentation.common.util.getAccessTokenOrNull
import com.polzzak_android.presentation.common.util.handleInvalidToken
import com.polzzak_android.presentation.common.util.toPx
import com.polzzak_android.presentation.component.PolzzakSnackBar
import com.polzzak_android.presentation.component.errorOf
import com.polzzak_android.presentation.component.toolbar.ToolbarData
import com.polzzak_android.presentation.component.toolbar.ToolbarHelper
import com.polzzak_android.presentation.component.toolbar.ToolbarIconInteraction
import com.polzzak_android.presentation.feature.notification.list.NotificationItemDecoration
import com.polzzak_android.presentation.feature.notification.list.NotificationListClickListener
import com.polzzak_android.presentation.feature.notification.list.NotificationListViewModel
import com.polzzak_android.presentation.feature.notification.list.item.NotificationEmptyItem
import com.polzzak_android.presentation.feature.notification.list.item.NotificationItem
import com.polzzak_android.presentation.feature.notification.list.item.NotificationSkeletonLoadingItem
import com.polzzak_android.presentation.feature.notification.list.model.NotificationModel
import com.polzzak_android.presentation.feature.root.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
abstract class BaseNotificationListFragment : BaseFragment<FragmentNotificationListBinding>(),
    NotificationListClickListener {
    override val layoutResId: Int = R.layout.fragment_notification_list

    private val mainViewModel by activityViewModels<MainViewModel>()

    @Inject
    lateinit var notificationListViewModelAssistedFactory: NotificationListViewModel.NotificationAssistedFactory
    private val notificationListViewModel by viewModels<NotificationListViewModel>(factoryProducer = {
        NotificationListViewModel.provideFactory(
            notificationListViewModelAssistedFactory,
            initAccessToken = getAccessTokenOrNull() ?: ""
        )
    })

    @get:IdRes
    abstract val actionToSettingFragment: Int
    abstract val memberType: MemberType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationListViewModel.initNotifications()
    }

    override fun initView() {
        super.initView()
        initToolbar()
        initSwipeRefreshLayout()
        initRecyclerView()
    }

    private fun initToolbar() {
        val toolbar = ToolbarHelper(
            data = ToolbarData(
                titleText = getString(R.string.common_notification),
                iconImageId = R.drawable.ic_setting,
                iconInteraction = object : ToolbarIconInteraction {
                    override fun onToolbarIconClicked() {
                        findNavController().navigate(actionToSettingFragment)
                    }
                }
            ), toolbar = binding.inToolbar
        ).apply { set() }
        toolbar.hideBackButton()
    }

    private fun initSwipeRefreshLayout() {
        with(binding.srlNotifications) {
            setOnRefreshListener {
                notificationListViewModel.refreshNotifications(
                    accessToken = getAccessTokenOrNull() ?: ""
                )
            }
            setColorSchemeResources(R.color.primary)
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
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING && !recyclerView.canScrollVertically(
                            1
                        )
                    ) notificationListViewModel.requestMoreNotifications(
                        getAccessTokenOrNull() ?: ""
                    )
                }
            })
        }
    }

    override fun initObserver() {
        super.initObserver()
        initNotificationObserver()
        initErrorEventObserver()
        initNotificationMessageObserver()
    }

    private fun initNotificationObserver() {
        notificationListViewModel.notificationLiveData.observe(viewLifecycleOwner) {
            val adapter =
                (binding.rvNotifications.adapter as? BindableItemAdapter) ?: return@observe
            val items = mutableListOf<BindableItem<*>>()
            binding.srlNotifications.isEnabled = it.data?.isRefreshable ?: false
            when (it) {
                is ModelState.Loading -> {
                    if (it.data?.items == null) {
                        items.addAll(createSkeletonLoadingItems())
                    } else {
                        items.addAll(createNotificationItems(data = it.data?.items))
                        if (!notificationListViewModel.isRefreshed) items.add(
                            LoadMoreLoadingSpinnerItem(
                                marginTopDp = 8
                            )
                        )
                    }
                }

                is ModelState.Success -> {
                    items.addAll(createNotificationItems(data = it.data.items))
                    binding.srlNotifications.isRefreshing = false
                }

                is ModelState.Error -> {
                    items.addAll(createNotificationItems(data = it.data?.items))
                    binding.srlNotifications.isRefreshing = false
                }
            }

            adapter.updateItem(item = items)
        }
    }

    private fun initErrorEventObserver() {
        notificationListViewModel.errorEventLiveData.observe(
            viewLifecycleOwner,
            EventWrapperObserver {
                when {
                    it.isAccessTokenException() -> handleInvalidToken()
                    else -> PolzzakSnackBar.errorOf(binding.root, it).show()
                }
            })
    }

    private fun initNotificationMessageObserver() {
        mainViewModel.refreshNotificationLiveEvent.observe(
            viewLifecycleOwner,
            EventWrapperObserver {
                notificationListViewModel.initNotifications()
            })
    }

    private fun createSkeletonLoadingItems() = List(LOADING_SKELETON_ITEM_COUNT) {
        NotificationSkeletonLoadingItem()
    }

    private fun createNotificationItems(data: List<NotificationModel>?): List<BindableItem<*>> {
        return if (data.isNullOrEmpty()) listOf(NotificationEmptyItem()) else data.map {
            NotificationItem(
                model = it,
                itemStateController = notificationListViewModel,
                clickListener = this@BaseNotificationListFragment
            )
        }
    }

    override fun onClickDeleteNotification(id: Int) {
        notificationListViewModel.deleteNotification(
            accessToken = getAccessTokenOrNull() ?: "",
            id = id
        )
    }

    override fun onClickFamilyRequestAcceptClick(model: NotificationModel) {
        notificationListViewModel.requestApproveLinkRequest(
            accessToken = getAccessTokenOrNull() ?: "",
            notificationModel = model
        )
    }

    override fun onClickFamilyRequestRejectClick(model: NotificationModel) {
        notificationListViewModel.requestRejectLinkRequest(
            accessToken = getAccessTokenOrNull() ?: "",
            notificationModel = model
        )
    }

    companion object {
        private const val LOADING_SKELETON_ITEM_COUNT = 5
        private const val NOTIFICATIONS_PADDING_DP = 16
        private const val NOTIFICATIONS_BETWEEN_MARGIN_DP = 8
    }
}