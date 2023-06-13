package com.polzzak_android.presentation.search.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentSearchBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.common.util.BindableItemAdapter
import com.polzzak_android.presentation.search.item.SearchMainRequestEmptyItem
import com.polzzak_android.presentation.search.item.SearchMainRequestItem
import com.polzzak_android.presentation.search.model.SearchPageTypeModel

abstract class BaseSearchFragment : BaseFragment<FragmentSearchBinding>() {
    override val layoutResId: Int = R.layout.fragment_search

    abstract val searchViewModel: BaseSearchViewModel
    abstract val typeString: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initView() {
        with(binding) {
            //TODO string resource로 변경
            tvTitle.text = "$typeString 찾기"
            etSearch.hint = "$typeString 검색"
            ivBtnClearText.setOnClickListener {
                etSearch.setText("")
            }
            tvBtnCancel.setOnClickListener {
                searchViewModel.setPage(SearchPageTypeModel.MAIN)
            }
            binding.root.setOnTouchListener { _, _ ->
                hideKeyboard()
                etSearch.clearFocus()
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
                        items.add(SearchMainRequestEmptyItem())
                    } else {
                        items.addAll(it.data.map { model -> SearchMainRequestItem(model = model) })
                    }
                }

                is ModelState.Error -> {}
            }
            adapter.updateItem(item = items)
        }
    }
}