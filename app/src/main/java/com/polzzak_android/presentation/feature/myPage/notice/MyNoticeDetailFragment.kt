package com.polzzak_android.presentation.feature.myPage.notice

import androidx.navigation.fragment.findNavController
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentMyNoticeDetailBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.util.getParcelableOrNull
import com.polzzak_android.presentation.component.toolbar.ToolbarData
import com.polzzak_android.presentation.component.toolbar.ToolbarHelper
import com.polzzak_android.presentation.feature.myPage.notice.model.MyNoticeModel
import java.time.format.DateTimeFormatter

class MyNoticeDetailFragment : BaseFragment<FragmentMyNoticeDetailBinding>() {
    override val layoutResId: Int = R.layout.fragment_my_notice_detail

    override fun initView() {
        super.initView()
        initToolbar()
        initContent()
    }

    private fun initToolbar() {
        ToolbarHelper(
            data = ToolbarData(
                popStack = findNavController(),
                titleText = getString(R.string.common_notice)
            ),
            toolbar = binding.inToolbar
        ).set()
    }

    private fun initContent() {
        val noticeModel =
            arguments?.getParcelableOrNull(ARGUMENT_NOTICE_KEY, MyNoticeModel::class.java) ?: return
        with(binding) {
            tvTitle.text = noticeModel.title
            tvDate.text = noticeModel.date.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"))
            tvContent.text = noticeModel.content
        }
    }

    companion object {
        const val ARGUMENT_NOTICE_KEY = "argument_notice_key"
    }
}