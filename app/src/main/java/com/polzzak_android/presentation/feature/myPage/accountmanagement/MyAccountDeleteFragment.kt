package com.polzzak_android.presentation.feature.myPage.accountmanagement

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.polzzak_android.R
import com.polzzak_android.data.remote.model.isAccessTokenException
import com.polzzak_android.databinding.FragmentMyAccountDeleteBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.model.ButtonCount
import com.polzzak_android.presentation.common.model.CommonButtonModel
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.util.SpannableBuilder
import com.polzzak_android.presentation.common.util.handleInvalidToken
import com.polzzak_android.presentation.common.util.logout
import com.polzzak_android.presentation.component.PolzzakSnackBar
import com.polzzak_android.presentation.component.dialog.CommonDialogContent
import com.polzzak_android.presentation.component.dialog.CommonDialogHelper
import com.polzzak_android.presentation.component.dialog.CommonDialogModel
import com.polzzak_android.presentation.component.dialog.DialogStyleType
import com.polzzak_android.presentation.component.dialog.OnButtonClickListener
import com.polzzak_android.presentation.component.errorOf
import com.polzzak_android.presentation.component.toolbar.ToolbarData
import com.polzzak_android.presentation.component.toolbar.ToolbarHelper
import com.polzzak_android.presentation.feature.myPage.accountmanagement.MyAccountManagementFragment.Companion.ARGUMENT_NICKNAME_KEY
import timber.log.Timber

class MyAccountDeleteFragment : BaseFragment<FragmentMyAccountDeleteBinding>() {
    override val layoutResId: Int = R.layout.fragment_my_account_delete

    private val myAccountDeleteViewModel by viewModels<MyAccountDeleteViewModel>()

    private var currentDialog: DialogFragment? = null

    override fun initView() {
        super.initView()
        initToolbar()
        with(binding) {
            val nickName = arguments?.getString(ARGUMENT_NICKNAME_KEY) ?: ""
            tvTitle.text = getString(R.string.my_account_management_delete_title, nickName)
            with(inMenuDeleteLink) {
                tvContent.text =
                    getText(R.string.my_account_management_delete_menu_delete_link)
                root.setOnClickListener {
                    myAccountDeleteViewModel.toggleDeleteLink()
                }
            }
            with(inMenuDeletePoint) {
                tvContent.text =
                    getText(R.string.my_account_management_delete_menu_delete_point)
                root.setOnClickListener {
                    myAccountDeleteViewModel.toggleDeletePoint()
                }
            }
            with(inMenuDeleteSocialAccountData) {
                tvContent.text =
                    getText(R.string.my_account_management_delete_menu_delete_social_account_data)
                root.setOnClickListener {
                    myAccountDeleteViewModel.toggleDeleteSocialAccountData()
                }
            }
            with(inMenuDeleteStampAndCoupon) {
                tvContent.text =
                    getText(R.string.my_account_management_delete_menu_delete_stamp_and_coupon)
                root.setOnClickListener {
                    myAccountDeleteViewModel.toggleDeleteStampAndCoupon()
                }
            }
            tvBtnDeleteAccount.setOnClickListener {
                val context = context ?: return@setOnClickListener
                val dialogTitleSpannable = SpannableBuilder.build(context) {
                    span(
                        text = getString(R.string.my_account_management_delete_dialog_title),
                        style = R.style.subtitle_18_600,
                        textColor = R.color.gray_700
                    )
                }
                val dialog = CommonDialogHelper.getInstance(
                    content = CommonDialogModel(
                        type = DialogStyleType.ALERT,
                        content = CommonDialogContent(title = dialogTitleSpannable),
                        button = CommonButtonModel(
                            buttonCount = ButtonCount.TWO,
                            positiveButtonText = getString(R.string.my_account_management_delete_dialog_btn_positive),
                            negativeButtonText = getString(R.string.my_account_management_delete_dialog_btn_negative)
                        )
                    ),
                    onConfirmListener = {
                        object : OnButtonClickListener {
                            override fun setBusinessLogic() {
                                myAccountDeleteViewModel.deleteAccount()
                            }

                            override fun getReturnValue(value: Any) {
                                //do nothing
                            }
                        }
                    }
                )
                showDialog(dialogFragment = dialog)
            }
        }
    }

    private fun initToolbar() {
        ToolbarHelper(
            data = ToolbarData(
                popStack = findNavController(),
                titleText = getString(R.string.my_account_management)
            ),
            toolbar = binding.inToolbar
        ).set()
    }

    override fun initObserver() {
        super.initObserver()
        myAccountDeleteViewModel.myAccountMenuLiveData.observe(viewLifecycleOwner) {
            with(binding) {
                inMenuDeleteLink.ivBtnCheck.isSelected = it.isCheckedDeleteLink
                inMenuDeletePoint.ivBtnCheck.isSelected = it.isCheckedDeletePoint
                inMenuDeleteSocialAccountData.ivBtnCheck.isSelected =
                    it.isCheckedDeleteSocialAccountData
                inMenuDeleteStampAndCoupon.ivBtnCheck.isSelected = it.isCheckedDeleteStampAndCoupon
                tvBtnDeleteAccount.isEnabled = it.isAllChecked()
            }
        }

        myAccountDeleteViewModel.deleteAccountLiveData.observe(viewLifecycleOwner) {
            val context = context ?: return@observe
            when (it) {
                is ModelState.Loading -> {
                    val dialogTitleSpannable = SpannableBuilder.build(context) {
                        span(
                            text = getString(R.string.my_account_management_delete_dialog_title),
                            style = R.style.subtitle_18_600,
                            textColor = R.color.gray_700
                        )
                    }
                    val loadingDialog = CommonDialogHelper.getInstance(
                        content = CommonDialogModel(
                            type = DialogStyleType.LOADING,
                            content = CommonDialogContent(title = dialogTitleSpannable),
                            button = CommonButtonModel(ButtonCount.ZERO)
                        )
                    )
                    showDialog(dialogFragment = loadingDialog)
                }

                is ModelState.Success -> {
                    dismissCurrentDialog()
                    Timber.d("회원탈퇴")
                    logout()
                }

                is ModelState.Error -> {
                    dismissCurrentDialog()
                    when {
                        it.exception.isAccessTokenException() -> handleInvalidToken()
                        else -> PolzzakSnackBar.errorOf(binding.root, it.exception).show()
                    }
                }
            }
        }
    }

    private fun showDialog(dialogFragment: DialogFragment) {
        currentDialog?.dismiss()
        currentDialog = dialogFragment
        dialogFragment.show(childFragmentManager, null)
    }

    private fun dismissCurrentDialog() {
        currentDialog?.dismiss()
        currentDialog = null
    }

    override fun onPause() {
        super.onPause()
        currentDialog?.dismiss()
    }
}