package com.polzzak_android.presentation.link.search.base

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.TextWatcher
import android.text.style.TextAppearanceSpan
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentSearchBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.model.ButtonCount
import com.polzzak_android.presentation.common.model.CommonButtonModel
import com.polzzak_android.presentation.common.model.CommonDialogContent
import com.polzzak_android.presentation.common.model.CommonDialogModel
import com.polzzak_android.presentation.common.model.DialogStyleType
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.common.util.BindableItemAdapter
import com.polzzak_android.presentation.common.util.getAccessTokenOrNull
import com.polzzak_android.presentation.common.widget.CommonDialogHelper
import com.polzzak_android.presentation.common.widget.OnButtonClickListener
import com.polzzak_android.presentation.link.item.LinkMainEmptyItem
import com.polzzak_android.presentation.link.item.LinkMainSentRequestItem
import com.polzzak_android.presentation.link.item.LinkRequestEmptyItem
import com.polzzak_android.presentation.link.item.LinkRequestGuideItem
import com.polzzak_android.presentation.link.item.LinkRequestLoadingItem
import com.polzzak_android.presentation.link.item.LinkRequestSuccessItem
import com.polzzak_android.presentation.link.model.LinkMemberType
import com.polzzak_android.presentation.link.model.LinkPageTypeModel
import com.polzzak_android.presentation.link.model.LinkRequestUserModel
import com.polzzak_android.presentation.link.search.SearchClickListener
import com.polzzak_android.presentation.link.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseSearchFragment : BaseFragment<FragmentSearchBinding>(), SearchClickListener {
    override val layoutResId: Int = R.layout.fragment_search

    protected abstract val targetLinkMemberType: LinkMemberType
    protected abstract val linkMemberType: LinkMemberType

    @get:IdRes
    protected abstract val actionMoveMainFragment: Int

    @Inject
    lateinit var searchViewModelAssistedFactory: SearchViewModel.SearchViewModelAssistedFactory

    private val searchViewModel by viewModels<SearchViewModel> {
        SearchViewModel.provideFactory(
            searchViewModelAssistedFactory = searchViewModelAssistedFactory,
            initAccessToken = getAccessTokenOrNull() ?: "",
            linkMemberType = linkMemberType,
            targetLinkMemberType = targetLinkMemberType
        )
    }

    private val targetLinkTypeStringOrEmpty
        get() = context?.getString(targetLinkMemberType.stringRes) ?: ""
    private val linkMemberTypeStringOrEmpty
        get() = context?.getString(linkMemberType.stringRes) ?: ""

    override fun initView() {
        with(binding) {
            //TODO string resource로 변경
            initCommonView()
            initMainPageView()
            initRequestPageView()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initCommonView() {
        with(binding) {
            tvTitle.text = "$targetLinkTypeStringOrEmpty 찾기"
            ivBtnClearText.setOnClickListener {
                etSearch.setText("")
            }
            tvBtnCancel.setOnClickListener {
                searchViewModel.setPage(LinkPageTypeModel.MAIN)
            }
            root.setOnTouchListener { _, _ ->
                hideKeyboard()
                false
            }
            ivBtnBack.setOnClickListener {
                //TODO back 버튼
            }
            initSearchEditTextView()
        }
    }

    private fun initSearchEditTextView() {
        with(binding.etSearch) {
            hint = "$targetLinkTypeStringOrEmpty 검색"
            setText(searchViewModel.searchQueryLiveData.value ?: "")
            setOnFocusChangeListener { _, isFocused ->
                if (isFocused) searchViewModel.setPage(page = LinkPageTypeModel.REQUEST)
            }
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    searchViewModel.setSearchQuery(query = p0?.toString() ?: "")
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
                        hideKeyboard()
                        searchViewModel.requestSearchUserWithNickName(
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
            //TODO string resource 적용
            setEnabledBtnComplete(isEmpty = true)
            rvRequestList.adapter = BindableItemAdapter()
            tvBtnComplete.setOnClickListener {
                findNavController().navigate(actionMoveMainFragment)
            }
        }
    }

    private fun setEnabledBtnComplete(isEmpty: Boolean) {
        with(binding.inMain.tvBtnComplete) {
            //TODO string resource 변경
            this.text = if (isEmpty) "나중에 할게요" else "$targetLinkTypeStringOrEmpty 찾기 완료"
            this.isSelected = !isEmpty
        }
    }

    private fun initRequestPageView() {
        with(binding.inRequest) {
            rvSearchResult.adapter = BindableItemAdapter()
        }
    }

    private fun hideKeyboard() {
        activity?.run {
            binding.etSearch.clearFocus()
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputManager?.hideSoftInputFromWindow(
                binding.etSearch.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    override fun initObserver() {
        searchViewModel.pageLiveData.observe(viewLifecycleOwner) {
            with(binding) {
                inMain.root.isVisible = (it == LinkPageTypeModel.MAIN)
                inRequest.root.isVisible = (it == LinkPageTypeModel.REQUEST)
                tvBtnCancel.isVisible = (it == LinkPageTypeModel.REQUEST)
            }
        }

        searchViewModel.searchQueryLiveData.observe(viewLifecycleOwner) {
            with(binding) {
                ivBtnClearText.isVisible = it.isNotEmpty()
            }
        }
        observeRequest()
        observeSearchUser()
    }

    private fun observeRequest() {
        searchViewModel.requestLiveData.observe(viewLifecycleOwner) {
            val requestListRecyclerView = binding.inMain.rvRequestList
            val adapter =
                (requestListRecyclerView.adapter as? BindableItemAdapter) ?: return@observe
            val items = mutableListOf<BindableItem<*>>()
            //TODO 로딩, 에러케이스 적용
            when (it) {
                is ModelState.Loading -> {}
                is ModelState.Success -> {
                    if (it.data.isEmpty()) {
                        //TODO string resource로 변경
                        items.add(LinkMainEmptyItem("보낸 요청이 없어요"))
                    } else {
                        items.addAll(it.data.map { model ->
                            LinkMainSentRequestItem(
                                model = model,
                                clickListener = this@BaseSearchFragment
                            )
                        })
                    }
                    setEnabledBtnComplete(isEmpty = it.data.isEmpty())
                }

                is ModelState.Error -> {}
            }
            adapter.updateItem(item = items)
        }
    }

    private fun observeSearchUser() {
        searchViewModel.searchUserLiveData.observe(viewLifecycleOwner) {
            val adapter =
                (binding.inRequest.rvSearchResult.adapter as? BindableItemAdapter) ?: return@observe
            val items = mutableListOf<BindableItem<*>>()
            when (it) {
                is ModelState.Loading -> {
                    //TODO string resource 적용
                    val nickName = searchViewModel.searchQueryLiveData.value ?: ""
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
    }

    override fun displayCancelRequestDialog(nickName: String, targetId: Int) {
        val context = binding.root.context
        //TODO 로딩있는 다이얼로그 추가, title spannable 적용(현재 적용안됨)
        val nickNameSpannable = SpannableString(nickName).apply {
            val nickNameSpan = TextAppearanceSpan(context, R.style.subtitle_20_600)
            setSpan(nickNameSpan, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        val contentSpannable = SpannableString("님에게 보낸\n연동 요청을 취소하시겠어요?").apply {
            val contentSpan = TextAppearanceSpan(context, R.style.body_13_500)
            setSpan(contentSpan, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        val title = TextUtils.concat(nickNameSpannable, contentSpannable).toString()
        displayLinkDialog(title = title, onPositiveButtonClickListener = {
            searchViewModel.cancelRequestLink(
                accessToken = getAccessTokenOrNull() ?: "",
                targetId = targetId
            )
        })

    }

    override fun displayRequestLinkDialog(nickName: String, targetId: Int) {
        val context = binding.root.context
        //TODO 로딩있는 다이얼로그 추가, title spannable style 적용(style은 임시로, 현재 다이얼로그는 style이 적용안됨)
        val nickNameSpannable = SpannableString(nickName).apply {
            val nickNameSpan = TextAppearanceSpan(context, R.style.subtitle_20_600)
            setSpan(nickNameSpan, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        val contentSpannable = SpannableString("님에게 보낸\n연동 요청을 보낼까요?").apply {
            val contentSpan = TextAppearanceSpan(context, R.style.body_13_500)
            setSpan(contentSpan, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        val title = TextUtils.concat(nickNameSpannable, contentSpannable).toString()
        displayLinkDialog(title = title, onPositiveButtonClickListener = {
            searchViewModel.requestLink(
                accessToken = getAccessTokenOrNull() ?: "",
                targetId = targetId
            )
        })
    }

    private fun displayLinkDialog(title: String, onPositiveButtonClickListener: () -> Unit) {
        CommonDialogHelper.getInstance(
            content = CommonDialogModel(
                type = DialogStyleType.ALERT,
                content = CommonDialogContent(
                    title = title,
                    body = null
                ),
                button = CommonButtonModel(
                    buttonCount = ButtonCount.TWO,
                    negativeButtonText = "아니요",
                    positiveButtonText = "네, 좋아요!"
                )
            ),
            onCancelListener = {
                object : OnButtonClickListener {
                    override fun setBusinessLogic() {
                        //do nothing
                    }

                    override fun getReturnValue(value: Any) {
                        //do nothing
                    }
                }
            },
            onButtonClickListener = {
                object : OnButtonClickListener {
                    override fun setBusinessLogic() {
                        onPositiveButtonClickListener.invoke()
                    }

                    override fun getReturnValue(value: Any) {
                        //do nothing
                    }
                }
            }
        ).show(childFragmentManager, null)
    }

    override fun cancelSearch() {
        searchViewModel.cancelSearchUserWithNickNameJob()
    }

    override fun cancelRequestLink(targetId: Int) {
        searchViewModel.cancelRequestLink(
            accessToken = getAccessTokenOrNull() ?: "",
            targetId = targetId
        )
    }
}