package com.polzzak_android.presentation.feature.myPage.protector.cs

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentProtectorCSBinding
import com.polzzak_android.databinding.ItemFaqBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.common.util.BindableItemAdapter
import com.polzzak_android.presentation.component.toolbar.ToolbarData
import com.polzzak_android.presentation.component.toolbar.ToolbarHelper
import com.polzzak_android.presentation.feature.myPage.model.FAQModel
import com.polzzak_android.presentation.feature.myPage.model.FAQType
import com.polzzak_android.presentation.feature.myPage.model.getFAQListForProtector

class ProtectorCSFragment : BaseFragment<FragmentProtectorCSBinding>() {
    override val layoutResId: Int = R.layout.fragment_protector_c_s

    private lateinit var toolbarHelper: ToolbarHelper

    private lateinit var adapter: BindableItemAdapter

    override fun setToolbar() {
        super.setToolbar()

        toolbarHelper = ToolbarHelper(
            data = ToolbarData(
                popStack = findNavController(),
                titleText = "고객센터"
            ),
            toolbar = binding.toolbar
        )
        toolbarHelper.set()
    }

    override fun initView() {
        super.initView()
        binding.fragment = this
        binding.isSearchMode = false
        binding.hasSearchedResult = true
        setAdapter()
        setSearchBarFocusListener()
    }

    private fun setAdapter() {
        binding.csFaqRc.adapter = BindableItemAdapter()
        adapter = (binding.csFaqRc.adapter as? BindableItemAdapter) ?: return

        updateAdapter(type = FAQType.STAMP, view = binding.csMenuStamp)
    }

    fun updateAdapter(type: FAQType, view: View) {
        initMenu()
        selectMenu(view)

        val faqList = getFAQListForProtector().filter {
            it.type == type
        }
        adapter.updateItem(item = faqList.map { model -> FAQItem(model = model) })
    }

    private fun updateAdapterWithSearchWord(word: String) {
        val faqList = getFAQListForProtector().filter {
            it.title.contains(word)
        }
        adapter.updateItem(item = faqList.map { model -> FAQItem(model = model) })

        binding.csFaqTitle.text = "검색 결과 ${faqList.size}"
        binding.hasSearchedResult = faqList.isNotEmpty()
    }

    inner class FAQItem(
        private val model: FAQModel,
    ) :
        BindableItem<ItemFaqBinding>() {
        override val layoutRes = R.layout.item_faq

        private var isExpanded = false

        private fun toggleExpand() {
            isExpanded = !isExpanded
        }

        private fun applyUiState(binding: ItemFaqBinding) {
            if (isExpanded) {
                with(binding) {
                    faqContent.visibility = View.VISIBLE
                    faqTitle.setTextColor(binding.root.context.getColor(R.color.black))
                    faqArrow.setImageResource(R.drawable.ic_arrow_up)
                }
            } else {
                with(binding) {
                    faqContent.visibility = View.GONE
                    faqTitle.setTextColor(binding.root.context.getColor(R.color.gray_700))
                    faqArrow.setImageResource(R.drawable.ic_arrow_down)
                }
            }
        }

        override fun bind(binding: ItemFaqBinding, position: Int) {
            with(binding) {
                faq = model
                applyUiState(binding)
                root.setOnClickListener {
                    toggleExpand()
                    applyUiState(binding)
                }
            }
        }

        override fun areItemsTheSame(other: BindableItem<*>): Boolean =
            other is FAQItem && other.model.title == this.model.title

        override fun areContentsTheSame(other: BindableItem<*>): Boolean =
            other is FAQItem && other.model == this.model
    }

    private fun initMenu() {
        val context = binding.root.context
        val textColor = context.getColor(R.color.gray_600)

        listOf(
            binding.csMenuStamp,
            binding.csMenuCoupon,
            binding.csMenuLinked,
            binding.csMenuPoint,
            binding.csMenuAccount
        ).forEach { view ->
            view.isSelected = false
            view.setTextColor(textColor)
        }
    }

    private fun selectMenu(view: View) {
        val context = view.context
        val isSelected = !view.isSelected
        view.isSelected = isSelected
        (view as TextView).setTextColor(
            context.getColor(if (isSelected) R.color.white else R.color.gray_600)
        )
    }

    private fun setSearchBarFocusListener() {
        binding.csSearchBar.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            setSearchedMode(hasFocus)
        }
    }

    fun setSearchedMode(isSearchedMode: Boolean) {
        binding.isSearchMode = isSearchedMode
        val textWatcher = setSearchBarTextWatcher()

        if (binding.isSearchMode == true) {
            with(binding) {
                updateAdapterWithSearchWord(word = binding.csSearchBar.text.toString())
                csSearchBar.addTextChangedListener(textWatcher)
            }
        } else {
            with(binding) {
                csFaqTitle.text = "자주 묻는 질문"
                hasSearchedResult = true
                csSearchBar.removeTextChangedListener(textWatcher)
                updateAdapter(type = FAQType.STAMP, view = binding.csMenuStamp)
            }
        }
    }

    private fun setSearchBarTextWatcher(): TextWatcher {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                updateAdapterWithSearchWord(word = s.toString())
                if (s.toString().isEmpty()) {
                    binding.csSearchBar.clearFocus()
                }
            }
        }

        return textWatcher
    }
}