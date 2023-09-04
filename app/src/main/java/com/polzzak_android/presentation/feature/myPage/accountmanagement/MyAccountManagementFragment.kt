package com.polzzak_android.presentation.feature.myPage.accountmanagement

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentMyAccountManagementBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.model.ButtonCount
import com.polzzak_android.presentation.common.model.CommonButtonModel
import com.polzzak_android.presentation.component.dialog.CommonDialogContent
import com.polzzak_android.presentation.component.dialog.CommonDialogHelper
import com.polzzak_android.presentation.component.dialog.CommonDialogModel
import com.polzzak_android.presentation.component.dialog.DialogStyleType
import com.polzzak_android.presentation.component.dialog.OnButtonClickListener
import com.polzzak_android.presentation.component.toolbar.ToolbarData
import com.polzzak_android.presentation.component.toolbar.ToolbarHelper
import com.polzzak_android.presentation.feature.root.MainViewModel
import com.polzzak_android.presentation.feature.root.host.RootNavigationController

class MyAccountManagementFragment : BaseFragment<FragmentMyAccountManagementBinding>() {
    override val layoutResId: Int = R.layout.fragment_my_account_management

    private val mainViewModel by activityViewModels<MainViewModel>()

    override fun initView() {
        super.initView()
        initToolbar()
        binding.tvBtnLogout.setOnClickListener {
            CommonDialogHelper.getInstance(
                content = CommonDialogModel(
                    type = DialogStyleType.ALERT,
                    content = CommonDialogContent(title = getString(R.string.my_account_management_logout_dialog_title)),
                    button = CommonButtonModel(
                        buttonCount = ButtonCount.TWO,
                        positiveButtonText = getString(R.string.my_account_management_logout_dialog_btn_positive)
                    )
                ),
                onConfirmListener = {
                    object : OnButtonClickListener {
                        override fun setBusinessLogic() {
                            findRootNavigationController()?.let {
                                mainViewModel.logout()
                                it.backToTheLoginFragment()
                            }
                        }

                        override fun getReturnValue(value: Any) {
                            //do nothing
                        }
                    }
                }
            ).show(childFragmentManager, null)
        }
        binding.tvBtnDeleteAccount.setOnClickListener {

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

    private fun findRootNavigationController(): RootNavigationController? {
        var parentFragment = parentFragment
        while (parentFragment != null) {
            if (parentFragment is RootNavigationController) return parentFragment
            parentFragment = parentFragment.parentFragment
        }
        return null
    }
}