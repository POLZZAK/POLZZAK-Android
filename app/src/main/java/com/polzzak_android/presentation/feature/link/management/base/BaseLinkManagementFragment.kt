package com.polzzak_android.presentation.feature.link.management.base

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentLinkManagementBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.common.util.BindableItemAdapter
import com.polzzak_android.presentation.common.util.getAccessTokenOrNull
import com.polzzak_android.presentation.common.util.hideKeyboard
import com.polzzak_android.presentation.common.util.toPx
import com.polzzak_android.presentation.feature.link.LinkClickListener
import com.polzzak_android.presentation.feature.link.LinkDialogFactory
import com.polzzak_android.presentation.feature.link.item.LinkMainLinkedUserItem
import com.polzzak_android.presentation.feature.link.item.LinkMainReceivedRequestItem
import com.polzzak_android.presentation.feature.link.item.LinkMainSentRequestItem
import com.polzzak_android.presentation.feature.link.item.LinkRequestEmptyItem
import com.polzzak_android.presentation.feature.link.item.LinkRequestGuideItem
import com.polzzak_android.presentation.feature.link.item.LinkRequestLoadingItem
import com.polzzak_android.presentation.feature.link.item.LinkRequestSuccessItem
import com.polzzak_android.presentation.feature.link.management.LinkManagementMainItemDecoration
import com.polzzak_android.presentation.feature.link.LinkViewModel
import com.polzzak_android.presentation.feature.link.management.item.LinkManagementMainEmptyItem
import com.polzzak_android.presentation.feature.link.management.model.LinkManagementMainTabTypeModel
import com.polzzak_android.presentation.feature.link.model.LinkMemberType
import com.polzzak_android.presentation.feature.link.model.LinkPageTypeModel
import com.polzzak_android.presentation.feature.link.model.LinkRequestUserModel
import com.polzzak_android.presentation.feature.link.model.LinkUserModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseLinkManagementFragment : BaseFragment<FragmentLinkManagementBinding>(),
    LinkClickListener {
    override val layoutResId: Int = R.layout.fragment_link_management

    protected abstract val targetLinkMemberType: LinkMemberType
    protected abstract val linkMemberType: LinkMemberType

    @Inject
    lateinit var linkViewModelAssistedFactory: LinkViewModel.LinkViewModelAssistedFactory
    private val linkViewModel by viewModels<LinkViewModel> {
        LinkViewModel.provideFactory(
            linkViewModelAssistedFactory = linkViewModelAssistedFactory,
            initAccessToken = getAccessTokenOrNull() ?: "",
            linkMemberType = linkMemberType,
            targetLinkMemberType = targetLinkMemberType
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
        initCommonView()
        initMainPageView()
        initRequestPageView()
    }

    private fun initCommonView() {
        with(binding) {
            ivBtnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            ivBtnClearText.setOnClickListener {
                etSearch.setText("")
            }
            tvBtnCancel.setOnClickListener {
                linkViewModel.resetSearchUserResult()
                linkViewModel.setPage(LinkPageTypeModel.MAIN)
            }
            root.setOnTouchListener { view, _ ->
                hideKeyboardAndClearFocus()
                view.performClick()
                false
            }
            initSearchEditTextView()
        }
    }

    private fun initSearchEditTextView() {
        with(binding.etSearch) {
            setText(linkViewModel.searchQueryLiveData.value ?: "")
            setOnFocusChangeListener { _, isFocused ->
                if (isFocused) linkViewModel.setPage(page = LinkPageTypeModel.REQUEST)
            }
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    linkViewModel.setSearchQuery(query = p0?.toString() ?: "")
                }
            })
            imeOptions = EditorInfo.IME_ACTION_SEARCH
            setOnEditorActionListener(object : TextView.OnEditorActionListener {
                override fun onEditorAction(
                    v: TextView?,
                    actionId: Int,
                    event: KeyEvent?
                ): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        hideKeyboardAndClearFocus()
                        linkViewModel.requestSearchUserWithNickName(
                            accessToken = getAccessTokenOrNull() ?: ""
                        )
                        return true
                    }
                    return false
                }
            })
        }
    }

    private fun hideKeyboardAndClearFocus() {
        hideKeyboard()
        binding.etSearch.clearFocus()
    }

    private fun initMainPageView() {
        with(binding.inMain) {
            val tabs = LinkManagementMainTabTypeModel.values()

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
                    linkViewModel.setMainTabType(tabType = tabType)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })
            rvLinked.adapter = BindableItemAdapter()
            rvReceived.adapter = BindableItemAdapter()
            rvSent.adapter = BindableItemAdapter()
            val itemMarginPx = MAIN_ITEM_MARGIN_DP.toPx(binding.root.context)
            val itemDecoration = LinkManagementMainItemDecoration(marginPx = itemMarginPx)
            rvLinked.addItemDecoration(itemDecoration)
            rvReceived.addItemDecoration(itemDecoration)
            rvSent.addItemDecoration(itemDecoration)
        }
    }

    private fun initRequestPageView() {
        with(binding.inRequest) {
            rvSearchResult.adapter = BindableItemAdapter()
            rvSearchResult.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    hideKeyboardAndClearFocus()
                    return false
                }

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                    //do nothing
                }

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                    //do nothing
                }
            })
        }
    }

    override fun initObserver() {
        super.initObserver()
        observeSearch()
        observeHomeTabType()
        observeLinkedUsers()
        observeReceivedRequest()
        observeSentRequest()
        observeDialogResult(
            liveData = linkViewModel.deleteLinkLiveData,
            contentStringRes = R.string.link_dialog_delete_link_content
        )
        observeDialogResult(
            liveData = linkViewModel.approveRequestLiveData,
            contentStringRes = R.string.link_dialog_approve_request_content
        )
        observeDialogResult(
            liveData = linkViewModel.rejectRequestLiveData,
            contentStringRes = R.string.link_dialog_reject_request_content
        )
        observeDialogResult(
            liveData = linkViewModel.cancelRequestLiveData,
            contentStringRes = R.string.link_dialog_cancel_request_content
        )
        observeRequest()
        observeDialogResult(
            liveData = linkViewModel.requestLinkLiveData,
            contentStringRes = R.string.link_dialog_request_content
        )
    }

    private fun observeSearch() {
        linkViewModel.pageLiveData.observe(viewLifecycleOwner) {
            with(binding) {
                if (it == LinkPageTypeModel.MAIN) {
                    hideKeyboardAndClearFocus()
                    inMain.root.bringToFront()
                } else {
                    inRequest.root.bringToFront()
                }
                tvBtnCancel.isVisible = (it == LinkPageTypeModel.REQUEST)
                etSearch.setText("")
                etSearch.hint = when (it) {
                    LinkPageTypeModel.MAIN -> getString(
                        R.string.link_management_main_hint,
                        targetLinkTypeStringOrEmpty
                    )

                    LinkPageTypeModel.REQUEST -> getString(R.string.link_management_request_hint)
                    else -> ""
                }
                ivIconSearch.isVisible = (it == LinkPageTypeModel.MAIN)
            }
        }
        linkViewModel.searchQueryLiveData.observe(viewLifecycleOwner) {
            with(binding) {
                ivBtnClearText.isVisible = it.isNotEmpty()
            }
        }
    }

    private fun observeHomeTabType() {
        linkViewModel.mainTabTypeLiveData.observe(viewLifecycleOwner) {
            with(binding.inMain) {
                rvLinked.isVisible = (it == LinkManagementMainTabTypeModel.LINKED)
                rvReceived.isVisible = (it == LinkManagementMainTabTypeModel.RECEIVED)
                rvSent.isVisible = (it == LinkManagementMainTabTypeModel.SENT)
            }
        }
    }

    private fun observeLinkedUsers() {
        linkViewModel.linkedUsersLiveData.observe(viewLifecycleOwner) {
            val adapter =
                (binding.inMain.rvLinked.adapter as? BindableItemAdapter) ?: return@observe
            val items = mutableListOf<BindableItem<*>>()
            when (it) {
                is ModelState.Loading -> {
                    //TODO 로딩
                }

                is ModelState.Success -> {
                    val linkedUserItems =
                        it.data.map { linkUserModel ->
                            LinkMainLinkedUserItem(
                                model = linkUserModel,
                                clickListener = this@BaseLinkManagementFragment
                            )
                        }.ifEmpty {
                            val content = getString(
                                R.string.link_management_empty_link,
                                targetLinkTypeStringOrEmpty
                            )
                            listOf(LinkManagementMainEmptyItem(content = content))
                        }
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
        linkViewModel.receivedRequestLiveData.observe(viewLifecycleOwner) {
            val adapter =
                (binding.inMain.rvReceived.adapter as? BindableItemAdapter) ?: return@observe
            val items = mutableListOf<BindableItem<*>>()
            when (it) {
                is ModelState.Loading -> {
                    //TODO 로딩
                }

                is ModelState.Success -> {
                    val receivedRequestItems =
                        it.data.map { linkUserModel ->
                            LinkMainReceivedRequestItem(
                                model = linkUserModel,
                                clickListener = this@BaseLinkManagementFragment
                            )
                        }.ifEmpty {
                            val content = getString(R.string.link_management_empty_received)
                            listOf(LinkManagementMainEmptyItem(content = content))
                        }
                    items.addAll(receivedRequestItems)
                    setTabUpdatedStatusVisibility(
                        tabTypeModel = LinkManagementMainTabTypeModel.RECEIVED,
                        isVisible = it.data.isNotEmpty()
                    )

                }

                is ModelState.Error -> {
                    //TODO 에러케이스
                }
            }
            adapter.updateItem(item = items)
        }
    }

    private fun observeSentRequest() {
        linkViewModel.sentRequestLiveData.observe(viewLifecycleOwner) {
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
                        }.ifEmpty {
                            val content = getString(R.string.link_management_empty_sent)
                            listOf(LinkManagementMainEmptyItem(content = content))
                        }
                    items.addAll(sentRequestItems)
                    setTabUpdatedStatusVisibility(
                        tabTypeModel = LinkManagementMainTabTypeModel.SENT,
                        isVisible = it.data.isNotEmpty()
                    )
                }

                is ModelState.Error -> {
                    //TODO 에러케이스
                }
            }
            adapter.updateItem(item = items)
        }
    }

    private fun setTabUpdatedStatusVisibility(
        tabTypeModel: LinkManagementMainTabTypeModel,
        isVisible: Boolean
    ) {
        val tabPosition = tabTypeModel.ordinal
        binding.inMain.tlTab.getTabAt(tabPosition)?.view?.findViewById<ImageView>(R.id.ivUpdatedStatus)?.isVisible =
            isVisible
    }

    private fun observeDialogResult(
        liveData: LiveData<ModelState<String>>,
        @StringRes contentStringRes: Int
    ) {
        liveData.observe(viewLifecycleOwner) {
            when (it) {
                is ModelState.Loading -> {
                    val context = binding.root.context
                    val nickName = it.data ?: ""
                    val loadingDialog = dialogFactory.createLoadingDialog(
                        context = context,
                        nickName = nickName,
                        content = context.getString(contentStringRes),
                    )
                    showDialog(newDialog = loadingDialog)
                }

                is ModelState.Success -> dismissDialog()
                is ModelState.Error -> {
                    //TODO 에러처리
                }
            }
        }
    }

    private fun observeRequest() {
        linkViewModel.searchUserLiveData.observe(viewLifecycleOwner) {
            val adapter =
                (binding.inRequest.rvSearchResult.adapter as? BindableItemAdapter) ?: return@observe
            val items = mutableListOf<BindableItem<*>>()
            when (it) {
                is ModelState.Loading -> {
                    val nickName = it.data?.user?.nickName ?: ""
                    items.add(
                        LinkRequestLoadingItem(
                            nickName = nickName,
                            clickListener = this@BaseLinkManagementFragment
                        )
                    )
                }

                is ModelState.Success -> {
                    val item = createRequestUserItem(model = it.data)
                    items.add(item)
                }

                is ModelState.Error -> {
                    //TODO 에러케이스 적용
                }
            }
            adapter.updateItem(items)
        }
    }

    private fun createRequestUserItem(model: LinkRequestUserModel): BindableItem<*> = when (model) {
        is LinkRequestUserModel.Empty -> LinkRequestEmptyItem(model = model)
        is LinkRequestUserModel.Guide -> LinkRequestGuideItem(model = model)
        is LinkRequestUserModel.Normal -> LinkRequestSuccessItem.newInstance(
            userModel = model,
            clickListener = this@BaseLinkManagementFragment
        )

        is LinkRequestUserModel.Sent -> LinkRequestSuccessItem.newInstance(
            userModel = model,
            clickListener = this@BaseLinkManagementFragment
        )

        is LinkRequestUserModel.Linked -> LinkRequestSuccessItem.newInstance(userModel = model)
        is LinkRequestUserModel.Received -> LinkRequestSuccessItem.newInstance(
            userModel = model,
            onClickMessageStringRes = R.string.link_management_request_to_received
        )
    }

    override fun displayCancelRequestDialog(linkUserModel: LinkUserModel) {
        val context = binding.root.context
        val cancelRequestDialog =
            dialogFactory.createLinkDialog(
                context = context,
                nickName = linkUserModel.nickName,
                content = context.getString(R.string.link_dialog_cancel_request_content),
                negativeButtonStringRes = R.string.link_dialog_btn_negative,
                positiveButtonStringRes = R.string.link_dialog_btn_positive_cancel_request,
                onPositiveButtonClickListener = {
                    linkViewModel.requestCancelLinkRequest(
                        accessToken = getAccessTokenOrNull() ?: "", linkUserModel = linkUserModel
                    )
                })
        showDialog(newDialog = cancelRequestDialog)
    }

    override fun displayRequestLinkDialog(linkUserModel: LinkUserModel) {
        val context = binding.root.context
        val requestLinkDialog =
            dialogFactory.createLinkDialog(
                context = context,
                nickName = linkUserModel.nickName,
                content = context.getString(R.string.link_dialog_request_content),
                negativeButtonStringRes = R.string.link_dialog_btn_negative,
                positiveButtonStringRes = R.string.link_dialog_btn_positive_request_link,
                onPositiveButtonClickListener = {
                    linkViewModel.requestLink(
                        accessToken = getAccessTokenOrNull() ?: "",
                        linkUserModel = linkUserModel
                    )
                })
        showDialog(newDialog = requestLinkDialog)
    }

    override fun displayDeleteLinkDialog(linkUserModel: LinkUserModel) {
        val context = binding.root.context
        val requestLinkDialog =
            dialogFactory.createLinkDialog(
                context = context,
                nickName = linkUserModel.nickName,
                content = context.getString(R.string.link_dialog_delete_link_content),
                negativeButtonStringRes = R.string.link_dialog_btn_negative,
                positiveButtonStringRes = R.string.link_dialog_btn_positive_delete_link,
                onPositiveButtonClickListener = {
                    linkViewModel.requestDeleteLink(
                        accessToken = getAccessTokenOrNull() ?: "", linkUserModel = linkUserModel
                    )
                })
        showDialog(newDialog = requestLinkDialog)
    }

    override fun displayApproveRequestDialog(linkUserModel: LinkUserModel) {
        val context = binding.root.context
        val approveRequestDialog =
            dialogFactory.createLinkDialog(
                context = context,
                nickName = linkUserModel.nickName,
                content = context.getString(R.string.link_dialog_approve_request_content),
                negativeButtonStringRes = R.string.link_dialog_btn_negative,
                positiveButtonStringRes = R.string.link_dialog_btn_positive_approve_request,
                onPositiveButtonClickListener = {
                    linkViewModel.requestApproveLinkRequest(
                        accessToken = getAccessTokenOrNull() ?: "", linkUserModel = linkUserModel
                    )
                })
        showDialog(newDialog = approveRequestDialog)
    }

    override fun displayRejectRequestDialog(linkUserModel: LinkUserModel) {
        val context = binding.root.context
        val rejectRequestDialog =
            dialogFactory.createLinkDialog(
                context = context,
                nickName = linkUserModel.nickName,
                content = context.getString(R.string.link_dialog_reject_request_content),
                negativeButtonStringRes = R.string.link_dialog_btn_negative,
                positiveButtonStringRes = R.string.link_dialog_btn_positive_reject_request,
                onPositiveButtonClickListener = {
                    linkViewModel.requestRejectLinkRequest(
                        accessToken = getAccessTokenOrNull() ?: "", linkUserModel = linkUserModel
                    )
                })
        showDialog(newDialog = rejectRequestDialog)
    }

    private fun showDialog(newDialog: DialogFragment) {
        dismissDialog()
        dialog = newDialog
        dialog?.show(childFragmentManager, null)
    }

    private fun dismissDialog() {
        dialog?.dismiss()
        dialog = null
    }

    override fun cancelSearch() {
        linkViewModel.cancelSearchUserWithNickNameJob()
    }

    override fun cancelRequestLink(linkUserModel: LinkUserModel) {
        linkViewModel.requestCancelLinkRequest(
            accessToken = getAccessTokenOrNull() ?: "",
            linkUserModel = linkUserModel
        )
    }

    companion object {
        private const val MAIN_ITEM_MARGIN_DP = 24
    }
}