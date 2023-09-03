package com.polzzak_android.presentation.feature.auth.signup

import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentSignupTermDetailBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.component.toolbar.ToolbarData
import com.polzzak_android.presentation.component.toolbar.ToolbarHelper

class SignUpTermDetailFragment : BaseFragment<FragmentSignupTermDetailBinding>() {
    override val layoutResId: Int = R.layout.fragment_signup_term_detail

    override fun initView() {
        super.initView()
        initToolbar()
        initWebView()
    }

    private fun initToolbar() {
        with(binding.inToolbar) {
            val title = arguments?.getString(ARGUMENT_TITLE_KEY) ?: ""
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
            val url = arguments?.getString(ARGUMENT_URL_KEY) ?: ""
            webViewClient = WebViewClient()
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            loadUrl(url)
        }
    }

    companion object {
        const val ARGUMENT_TITLE_KEY = "argument_title_key"
        const val ARGUMENT_URL_KEY = "argument_url_key"
    }
}