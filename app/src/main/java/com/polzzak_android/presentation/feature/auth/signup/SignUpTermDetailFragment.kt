package com.polzzak_android.presentation.feature.auth.signup

import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentSignupTermDetailBinding
import com.polzzak_android.presentation.common.base.BaseFragment

class SignUpTermDetailFragment : BaseFragment<FragmentSignupTermDetailBinding>() {
    override val layoutResId: Int = R.layout.fragment_signup_term_detail

    override fun initView() {
        super.initView()
        with(binding) {
            ivBtnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
        initWebView()
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