package com.polzzak_android.presentation.auth.signup

import androidx.navigation.fragment.findNavController
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentSignupTermDetailBinding
import com.polzzak_android.presentation.common.base.BaseFragment

class SignUpTermDetailFragment : BaseFragment<FragmentSignupTermDetailBinding>() {
    override val layoutResId: Int = R.layout.fragment_signup_term_detail

    override fun initView() {
        super.initView()
        with(binding) {
            val title = arguments?.getString(ARGUMENT_TITLE_KEY) ?: ""
            val content = arguments?.getString(ARGUMENT_CONTENT_KEY) ?: ""
            tvTitle.text = title
            tvContent.text = content
            ivBtnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    companion object {
        const val ARGUMENT_TITLE_KEY = "argument_title_key"
        const val ARGUMENT_CONTENT_KEY = "argument_content_key"
    }
}