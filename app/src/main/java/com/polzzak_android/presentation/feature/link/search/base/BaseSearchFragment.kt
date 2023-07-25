package com.polzzak_android.presentation.feature.link.search.base

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.addCallback
import androidx.annotation.IdRes
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentSearchBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.common.util.BindableItemAdapter
import com.polzzak_android.presentation.common.util.getAccessTokenOrNull
import com.polzzak_android.presentation.common.util.hideKeyboard
import com.polzzak_android.presentation.common.util.shotBackPressed
import com.polzzak_android.presentation.common.util.toPx
import com.polzzak_android.presentation.feature.link.LinkClickListener
import com.polzzak_android.presentation.feature.link.LinkDialogFactory
import com.polzzak_android.presentation.feature.link.LinkViewModel
import com.polzzak_android.presentation.feature.link.item.LinkMainHeaderItem
import com.polzzak_android.presentation.feature.link.item.LinkMainSentRequestItem
import com.polzzak_android.presentation.feature.link.item.LinkRequestEmptyItem
import com.polzzak_android.presentation.feature.link.item.LinkRequestGuideItem
import com.polzzak_android.presentation.feature.link.item.LinkRequestLoadingItem
import com.polzzak_android.presentation.feature.link.item.LinkRequestSuccessItem
import com.polzzak_android.presentation.feature.link.model.LinkMemberType
import com.polzzak_android.presentation.feature.link.model.LinkPageTypeModel
import com.polzzak_android.presentation.feature.link.model.LinkRequestUserModel
import com.polzzak_android.presentation.feature.link.model.LinkUserModel
import com.polzzak_android.presentation.feature.link.search.SearchMainItemDecoration
import com.polzzak_android.presentation.feature.link.search.item.SearchMainEmptyItem
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseSearchFragment : BaseFragment<FragmentSearchBinding>(), LinkClickListener {
    override val layoutResId: Int = R.layout.fragment_search

    protected abstract val targetLinkMemberType: LinkMemberType
    protected abstract val linkMemberType: LinkMemberType

    @get:IdRes
    protected abstract val actionMoveMainFragment: Int

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
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {
            shotBackPressed()
        }
        initCommonView()
        initMainPageView()
        initRequestPageView()
    }

    private fun initCommonView() {
        with(binding) {
            tvTitle.text = context?.getString(R.string.search_title, targetLinkTypeStringOrEmpty)
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
            hint = context?.getString(R.string.search_main_hint, targetLinkTypeStringOrEmpty)
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

    private fun initMainPageView() {
        with(binding.inMain) {
            rvRequestList.adapter = BindableItemAdapter()
            val betweenMarginPx = MAIN_REQUEST_ITEM_BETWEEN_MARGIN_DP.toPx(binding.root.context)
            val bottomMarginPx = MAIN_REQUEST_ITEM_BOTTOM_MARGIN_DP.toPx(binding.root.context)
            rvRequestList.addItemDecoration(
                SearchMainItemDecoration(
                    betweenMarginPx = betweenMarginPx,
                    bottomMarginPx = bottomMarginPx
                )
            )
            tvBtnComplete.setOnClickListener {
                findNavController().navigate(actionMoveMainFragment)
            }
        }
    }

    private fun setEnabledBtnComplete(isEmpty: Boolean) {
        with(binding.inMain.tvBtnComplete) {
            val context = binding.root.context
            this.text =
                if (isEmpty) context.getString(R.string.common_do_later) else context.getString(
                    R.string.search_main_search_complete,
                    targetLinkTypeStringOrEmpty
                )
            this.isSelected = !isEmpty
        }
    }

    private fun initRequestPageView() {
        with(binding.inRequest) {
            rvSearchResult.adapter = BindableItemAdapter()
            rvSearchResult.addOnItemTouchListener(object : OnItemTouchListener {
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

    private fun hideKeyboardAndClearFocus() {
        hideKeyboard()
        binding.etSearch.clearFocus()
    }

    override fun initObserver() {
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
                ivIconSearch.isVisible = (it == LinkPageTypeModel.MAIN)
            }
        }

        linkViewModel.searchQueryLiveData.observe(viewLifecycleOwner) {
            with(binding) {
                ivBtnClearText.isVisible = it.isNotEmpty()
            }
        }
        linkViewModel.requestLinkLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ModelState.Loading -> {
                    val context = binding.root.context
                    val nickName = it.data ?: ""
                    val loadingDialog = dialogFactory.createLoadingDialog(
                        context = context,
                        nickName = nickName,
                        content = context.getString(R.string.link_dialog_request_content),
                    )
                    showDialog(newDialog = loadingDialog)
                }

                is ModelState.Success -> dismissDialog()
                is ModelState.Error -> {
                    //TODO 에러처리
                }
            }
        }
        linkViewModel.cancelRequestLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ModelState.Loading -> {
                    val context = binding.root.context
                    val nickName = it.data ?: ""
                    val loadingDialog = dialogFactory.createLoadingDialog(
                        context = context,
                        nickName = nickName,
                        content = context.getString(R.string.link_dialog_cancel_request_content),
                    )
                    showDialog(newDialog = loadingDialog)
                }

                is ModelState.Success -> dismissDialog()
                is ModelState.Error -> {
                    //TODO 에러처리
                }
            }

        }
        observeMain()
        observeRequest()
    }

    private fun observeMain() {
        linkViewModel.sentRequestLiveData.observe(viewLifecycleOwner) {
            val requestListRecyclerView = binding.inMain.rvRequestList
            val adapter =
                (requestListRecyclerView.adapter as? BindableItemAdapter) ?: return@observe
            val items = mutableListOf<BindableItem<*>>()
            //TODO 로딩, 에러케이스 적용
            when (it) {
                is ModelState.Loading -> {}
                is ModelState.Success -> {
                    val headerTitleStr = getString(R.string.search_main_header_text)
                    items.add(LinkMainHeaderItem(text = headerTitleStr))
                    if (it.data.isEmpty()) {
                        val emptyContentStr = getString(R.string.search_main_empty_text)
                        items.add(SearchMainEmptyItem(text = emptyContentStr))
                    } else {
                        items.addAll(it.data.map { model ->
                            LinkMainSentRequestItem(
                                model = model,
                                clickListener = this@BaseSearchFragment
                            )
                        })
                    }
                }

                is ModelState.Error -> {}
            }
            setEnabledBtnComplete(isEmpty = it.data.isNullOrEmpty())
            adapter.updateItem(item = items)
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
                            clickListener = this@BaseSearchFragment
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
            clickListener = this@BaseSearchFragment
        )

        is LinkRequestUserModel.Sent -> LinkRequestSuccessItem.newInstance(
            userModel = model,
            clickListener = this@BaseSearchFragment
        )

        is LinkRequestUserModel.Linked -> LinkRequestSuccessItem.newInstance(userModel = model)
        is LinkRequestUserModel.Received -> LinkRequestSuccessItem.newInstance(
            userModel = model,
            onClickMessageStringRes = R.string.search_request_request_to_received
        )
    }

    override fun displayCancelRequestDialog(linkUserModel: LinkUserModel) {
        val context = binding.root.context
        val cancelLinkDialog =
            dialogFactory.createLinkDialog(
                context = context,
                nickName = linkUserModel.nickName,
                content = context.getString(R.string.link_dialog_cancel_request_content),
                negativeButtonStringRes = R.string.link_dialog_btn_negative,
                positiveButtonStringRes = R.string.link_dialog_btn_positive_cancel_request,
                onPositiveButtonClickListener = {
                    cancelRequestLink(linkUserModel = linkUserModel)
                })
        showDialog(newDialog = cancelLinkDialog)
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
        private const val MAIN_REQUEST_ITEM_BETWEEN_MARGIN_DP = 24
        private const val MAIN_REQUEST_ITEM_BOTTOM_MARGIN_DP = 20
    }
}