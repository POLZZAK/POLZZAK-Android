package com.polzzak_android.presentation.link.management.base

import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.polzzak_android.R
import com.polzzak_android.common.util.convertDp2Px
import com.polzzak_android.databinding.FragmentLinkManagementBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.common.util.BindableItemAdapter
import com.polzzak_android.presentation.common.util.getAccessTokenOrNull
import com.polzzak_android.presentation.link.LinkDialogFactory
import com.polzzak_android.presentation.link.LinkMainClickListener
import com.polzzak_android.presentation.link.item.LinkMainLinkedUserItem
import com.polzzak_android.presentation.link.item.LinkMainReceivedRequestItem
import com.polzzak_android.presentation.link.item.LinkMainSentRequestItem
import com.polzzak_android.presentation.link.management.LinkManagementMainItemDecoration
import com.polzzak_android.presentation.link.management.LinkManagementViewModel
import com.polzzak_android.presentation.link.management.model.LinkManagementMainTabTypeModel
import com.polzzak_android.presentation.link.model.LinkMemberType
import com.polzzak_android.presentation.link.model.LinkUserModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseLinkManagementFragment : BaseFragment<FragmentLinkManagementBinding>(),
    LinkMainClickListener {
    override val layoutResId: Int = R.layout.fragment_link_management

    protected abstract val targetLinkMemberType: LinkMemberType
    protected abstract val linkMemberType: LinkMemberType

    @Inject
    lateinit var linkManagementViewModelAssistedFactory: LinkManagementViewModel.LinkManagementViewModelAssistedFactory
    private val linkManagementViewModel by viewModels<LinkManagementViewModel> {
        LinkManagementViewModel.provideFactory(
            linkManagementViewModelAssistedFactory = linkManagementViewModelAssistedFactory,
            initAccessToken = getAccessTokenOrNull() ?: "",
        )
    }

    private val targetLinkTypeStringOrEmpty
        get() = context?.getString(targetLinkMemberType.stringRes) ?: ""
    private val linkMemberTypeStringOrEmpty
        get() = context?.getString(linkMemberType.stringRes) ?: ""

    private val dialogFactory: LinkDialogFactory = LinkDialogFactory()
    private var dialog: DialogFragment? = null

    override fun initView() {
        super.initView()
        with(binding) {
            ivBtnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
        initHomeView()
    }

    private fun initHomeView() {
        with(binding.inMain) {
            val tabs = listOf(
                LinkManagementMainTabTypeModel.LINKED,
                LinkManagementMainTabTypeModel.RECEIVED,
                LinkManagementMainTabTypeModel.SENT
            )

            tabs.forEach {
                val tab = tlTab.newTab().setCustomView(R.layout.item_link_management_tab)
                    .setText(it.titleStringRes)
                tab.view.findViewById<TextView>(R.id.tvTitle)
                    .setText(it.titleStringRes)
                tlTab.addTab(tab)
            }

            tlTab.addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val position = tab?.position ?: return
                    val tabType = tabs.getOrNull(position) ?: return
                    linkManagementViewModel.setMainTabType(tabType = tabType)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })
            rvLinked.adapter = BindableItemAdapter()
            rvReceived.adapter = BindableItemAdapter()
            rvSent.adapter = BindableItemAdapter()
            val itemMarginPx =
                convertDp2Px(context = binding.root.context, dp = MAIN_ITEM_MARGIN_DP)
            val itemDecoration = LinkManagementMainItemDecoration(marginPx = itemMarginPx)
            rvLinked.addItemDecoration(itemDecoration)
            rvReceived.addItemDecoration(itemDecoration)
            rvSent.addItemDecoration(itemDecoration)
        }
    }

    override fun initObserver() {
        super.initObserver()
        observeHomeTabType()
        observeLinkRequestStatus()
        observeLinkedUsers()
        observeReceivedRequest()
        observeSentRequest()
    }

    private fun observeLinkRequestStatus() {
        linkManagementViewModel.requestStatusLiveData.observe(viewLifecycleOwner) {
            with(binding.inMain) {
                when (it) {
                    is ModelState.Success -> {
                        repeat(tlTab.tabCount) { position ->
                            tlTab.getTabAt(position)?.view?.findViewById<ImageView>(R.id.ivUpdatedStatus)?.isVisible =
                                it.data.itemsVisible.getOrElse(position) { false }
                        }
                    }

                    else -> {
                        //TODO 에러처리
                    }
                }
            }
        }
    }

    private fun observeHomeTabType() {
        linkManagementViewModel.mainTabTypeLiveData.observe(viewLifecycleOwner) {
            with(binding.inMain) {
                rvLinked.isVisible = (it == LinkManagementMainTabTypeModel.LINKED)
                rvReceived.isVisible = (it == LinkManagementMainTabTypeModel.RECEIVED)
                rvSent.isVisible = (it == LinkManagementMainTabTypeModel.SENT)
                when (it) {
                    LinkManagementMainTabTypeModel.LINKED -> linkManagementViewModel.requestLinkedUsers()
                    LinkManagementMainTabTypeModel.RECEIVED -> linkManagementViewModel.requestReceivedRequest()
                    LinkManagementMainTabTypeModel.SENT -> linkManagementViewModel.requestSentRequest()
                    else -> {
                        //do nothing
                    }
                }
            }
        }
    }

    private fun observeLinkedUsers() {
        linkManagementViewModel.linkedUsersLiveData.observe(viewLifecycleOwner) {
            val adapter =
                (binding.inMain.rvLinked.adapter as? BindableItemAdapter) ?: return@observe
            val items = mutableListOf<BindableItem<*>>()
            when (it) {
                is ModelState.Loading -> {
                    //TODO 로딩
                }

                is ModelState.Success -> {
                    val linkedUserItems =
                        it.data.map { linkUserModel -> LinkMainLinkedUserItem(model = linkUserModel) }
                    items.addAll(linkedUserItems)
                }

                is ModelState.Error -> {
                    //TODO 에러케이스
                }
            }
            adapter.updateItem(item = items)
        }
    }

    private fun observeReceivedRequest() {
        linkManagementViewModel.receivedRequestLiveData.observe(viewLifecycleOwner) {
            val adapter =
                (binding.inMain.rvReceived.adapter as? BindableItemAdapter) ?: return@observe
            val items = mutableListOf<BindableItem<*>>()
            when (it) {
                is ModelState.Loading -> {
                    //TODO 로딩
                }

                is ModelState.Success -> {
                    val receivedRequestItems =
                        it.data.map { linkUserModel -> LinkMainReceivedRequestItem(model = linkUserModel) }
                    items.addAll(receivedRequestItems)
                }

                is ModelState.Error -> {
                    //TODO 에러케이스
                }
            }
            adapter.updateItem(item = items)
        }
    }

    private fun observeSentRequest() {
        linkManagementViewModel.sentRequestLiveData.observe(viewLifecycleOwner) {
            val adapter =
                (binding.inMain.rvSent.adapter as? BindableItemAdapter) ?: return@observe
            val items = mutableListOf<BindableItem<*>>()
            when (it) {
                is ModelState.Loading -> {
                    //TODO 로딩
                }

                is ModelState.Success -> {
                    val sentRequestItems =
                        it.data.map { linkUserModel ->
                            LinkMainSentRequestItem(
                                model = linkUserModel,
                                clickListener = this@BaseLinkManagementFragment
                            )
                        }
                    items.addAll(sentRequestItems)
                }

                is ModelState.Error -> {
                    //TODO 에러케이스
                }
            }
            adapter.updateItem(item = items)
        }
    }

    override fun displayCancelRequestDialog(linkUserModel: LinkUserModel) {

    }

    override fun displayRequestLinkDialog(linkUserModel: LinkUserModel) {}

    override fun cancelRequestLink(linkUserModel: LinkUserModel) {}

    override fun cancelSearch() {}

    companion object {
        private const val MAIN_ITEM_MARGIN_DP = 24
    }
}