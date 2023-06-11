package com.polzzak_android.presentation.search.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.polzzak_android.databinding.FragmentSearchBinding
import com.polzzak_android.presentation.search.model.SearchPageTypeModel

abstract class BaseSearchDialogFragment : DialogFragment() {
    private var _binding: FragmentSearchBinding? = null
    protected val binding get() = _binding!!

    abstract val searchViewModel: BaseSearchViewModel
    abstract val typeString: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()
        isCancelable = false
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
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

    private fun initObserver() {
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

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}