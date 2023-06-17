package com.polzzak_android.presentation.link.search.base

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentSearchBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.common.util.BindableItemAdapter
import com.polzzak_android.presentation.common.util.getAccessTokenOrNull
import com.polzzak_android.presentation.link.item.LinkMainEmptyItem
import com.polzzak_android.presentation.link.item.LinkMainSentRequestItem
import com.polzzak_android.presentation.link.item.LinkRequestEmptyItem
import com.polzzak_android.presentation.link.item.LinkRequestGuideItem
import com.polzzak_android.presentation.link.item.LinkRequestLoadingItem
import com.polzzak_android.presentation.link.item.LinkRequestSuccessItem
import com.polzzak_android.presentation.link.model.LinkMemberType
import com.polzzak_android.presentation.link.model.LinkPageTypeModel
import com.polzzak_android.presentation.link.model.LinkRequestUserModel
import com.polzzak_android.presentation.link.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseSearchFragment : BaseFragment<FragmentSearchBinding>(), BaseSearchClickListener {
    override val layoutResId: Int = R.layout.fragment_search

    protected abstract val targetLinkMemberType: LinkMemberType
    protected abstract val linkMemberType: LinkMemberType

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

    @SuppressLint("ClickableViewAccessibility")
    override fun initView() {
        with(binding) {
            //TODO string resource로 변경
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
            initMainPageView()
            initRequestPageView()
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
            tvBtnComplete.text = "나중에 할게요"
            rvRequestList.adapter = BindableItemAdapter()
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
                    items.add(LinkRequestLoadingItem(nickName = nickName))
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
        is LinkRequestUserModel.Normal -> LinkRequestSuccessItem.newInstance(userModel = model)
        is LinkRequestUserModel.Sent -> LinkRequestSuccessItem.newInstance(userModel = model)
        is LinkRequestUserModel.Linked -> LinkRequestSuccessItem.newInstance(userModel = model)
    }

    override fun displayCancelRequestDialog() {
        //TODO 연동취소 dialog

    }
}