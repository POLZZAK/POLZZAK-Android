package com.polzzak_android.presentation.feature.myPage.accountmanagement

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentMyAccountManagementBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.model.ButtonCount
import com.polzzak_android.presentation.common.model.CommonButtonModel
import com.polzzak_android.presentation.common.util.SpannableBuilder
import com.polzzak_android.presentation.common.util.logout
import com.polzzak_android.presentation.component.dialog.CommonDialogContent
import com.polzzak_android.presentation.component.dialog.CommonDialogHelper
import com.polzzak_android.presentation.component.dialog.CommonDialogModel
import com.polzzak_android.presentation.component.dialog.DialogStyleType
import com.polzzak_android.presentation.component.dialog.OnButtonClickListener
import com.polzzak_android.presentation.component.toolbar.ToolbarData
import com.polzzak_android.presentation.component.toolbar.ToolbarHelper

class MyAccountManagementFragment : BaseFragment<FragmentMyAccountManagementBinding>() {
    override val layoutResId: Int = R.layout.fragment_my_account_management

    override fun initView() {
        super.initView()
        initToolbar()
        binding.tvBtnLogout.setOnClickListener {
            val context = context ?: return@setOnClickListener
            val dialogTitleSpannable = SpannableBuilder.build(context) {
                span(
                    getString(R.string.my_account_management_logout_dialog_title),
                    style = R.style.subtitle_18_600,
                    textColor = R.color.gray_700
                )
            }
            CommonDialogHelper.getInstance(
                content = CommonDialogModel(
                    type = DialogStyleType.ALERT,
                    content = CommonDialogContent(title = dialogTitleSpannable),
                    button = CommonButtonModel(
                        buttonCount = ButtonCount.TWO,
                        positiveButtonText = getString(R.string.my_account_management_logout_dialog_btn_positive)
                    )
                ),
                onConfirmListener = {
                    object : OnButtonClickListener {
                        override fun setBusinessLogic() {
                            logout()
                        }

                        override fun getReturnValue(value: Any) {
                            //do nothing
                        }
                    }
                }
            ).show(childFragmentManager, null)
        }
        binding.tvBtnDeleteAccount.setOnClickListener {
            val nickName = arguments?.getString(ARGUMENT_NICKNAME_KEY) ?: ""
            val bundle = Bundle().apply {
                putString(ARGUMENT_NICKNAME_KEY, nickName)
            }
            findNavController().navigate(
                R.id.action_myAccountManagementFragment_to_myAccountDeleteFragment,
                bundle
            )
        }
    }

    private fun initToolbar() {
        ToolbarHelper(
            data = ToolbarData(
                popStack = findNavController(),
                titleText = getString(R.string.my_account_management)
            ), toolbar = binding.inToolbar
        ).set()
    }

    companion object {
        const val ARGUMENT_NICKNAME_KEY = "argument_nickname_key"
    }
}