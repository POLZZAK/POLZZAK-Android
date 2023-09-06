package com.polzzak_android.presentation.feature.term

import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentTermDetailBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.util.getParcelableOrNull
import com.polzzak_android.presentation.component.toolbar.ToolbarData
import com.polzzak_android.presentation.component.toolbar.ToolbarHelper
import com.polzzak_android.presentation.feature.term.model.TermType

class TermDetailFragment : BaseFragment<FragmentTermDetailBinding>() {
    override val layoutResId: Int = R.layout.fragment_term_detail

    private val type by lazy {
        arguments?.getParcelableOrNull(ARGUMENT_TYPE_KEY, TermType::class.java)
    }

    override fun initView() {
        super.initView()
        initToolbar()
        initWebView()
    }

    private fun initToolbar() {
        with(binding.inToolbar) {
            val title = type?.let { getString(it.titleStrRes) } ?: ""
            val toolbarData =
                ToolbarData(popStack = findNavController(), titleText = title)
            ToolbarHelper(data = toolbarData, toolbar = this).run {
                set()
                updateToolbarBackgroundColor(R.color.white)
            }
        }
    }

    private fun initWebView() {
        with(binding.wbContent) {
            val url = type?.url ?: ""
            webViewClient = WebViewClient()
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            loadUrl(url)
        }
    }

    companion object {
        const val ARGUMENT_TYPE_KEY = "argument_type_key"
    }
}