package com.polzzak_android.presentation.link.search.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.view.isVisible
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentSearchBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.common.util.BindableItemAdapter
import com.polzzak_android.presentation.link.item.LinkMainEmptyItem
import com.polzzak_android.presentation.link.item.LinkMainSentRequestItem
import com.polzzak_android.presentation.link.search.model.SearchPageTypeModel
import timber.log.Timber

abstract class BaseSearchFragment : BaseFragment<FragmentSearchBinding>(), BaseSearchClickListener {
    override val layoutResId: Int = R.layout.fragment_search

    abstract val searchViewModel: BaseSearchViewModel
    abstract val targetString: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initView() {
        with(binding) {
            //TODO string resource로 변경
            tvTitle.text = "$targetString 찾기"
            ivBtnClearText.setOnClickListener {
                etSearch.setText("")
            }
            tvBtnCancel.setOnClickListener {
                searchViewModel.setPage(SearchPageTypeModel.MAIN)
            }
            binding.root.setOnTouchListener { _, _ ->
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
            hint = "$targetString 검색"
            setText(searchViewModel.searchQueryLiveData.value ?: "")
            setOnFocusChangeListener { _, isFocused ->
                if (isFocused) searchViewModel.setPage(page = SearchPageTypeModel.REQUEST)
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
                        Timber.d("${v?.text}")
                        return true
                    }
                    return false
                }
            })
        }
    }

    private fun initMainPageView() {
        with(binding.inMain) {
            tvBtnComplete.text = "나중에 할게요"
            rvRequestList.adapter = BindableItemAdapter()
        }
    }

    private fun initRequestPageView() {
        with(binding.inRequest) {

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
                inMain.root.isVisible = (it == SearchPageTypeModel.MAIN)
                inRequest.root.isVisible = (it == SearchPageTypeModel.REQUEST)
                tvBtnCancel.isVisible = (it == SearchPageTypeModel.REQUEST)
            }
        }

        searchViewModel.searchQueryLiveData.observe(viewLifecycleOwner) {
            with(binding) {
                ivBtnClearText.isVisible = it.isNotEmpty()
            }
        }

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

    override fun displayCancelRequestDialog() {
        //TODO 연동취소 dialog

    }
}