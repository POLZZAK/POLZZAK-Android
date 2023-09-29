package com.polzzak_android.presentation.feature.myPage.protector

import android.net.Uri
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import androidx.core.text.toSpannable
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentProtectorProfileModifyBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.model.ButtonCount
import com.polzzak_android.presentation.common.model.CommonButtonModel
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.util.PermissionManager
import com.polzzak_android.presentation.common.util.getAccessTokenOrNull
import com.polzzak_android.presentation.common.util.getPermissionManagerOrNull
import com.polzzak_android.presentation.component.dialog.CommonDialogContent
import com.polzzak_android.presentation.component.dialog.CommonDialogHelper
import com.polzzak_android.presentation.component.dialog.CommonDialogModel
import com.polzzak_android.presentation.component.dialog.DialogStyleType
import com.polzzak_android.presentation.component.dialog.OnButtonClickListener
import com.polzzak_android.presentation.component.toolbar.ToolbarData
import com.polzzak_android.presentation.component.toolbar.ToolbarHelper
import com.polzzak_android.presentation.component.toolbar.ToolbarIconInteraction
import com.polzzak_android.presentation.feature.auth.signup.PhotoPicker
import timber.log.Timber

class ProtectorProfileModifyFragment : BaseFragment<FragmentProtectorProfileModifyBinding>(),
    ToolbarIconInteraction {
    override val layoutResId: Int = R.layout.fragment_protector_profile_modify

    private lateinit var toolbarHelper: ToolbarHelper
    private var photoPicker: PhotoPicker? = null
    private val loadingDialog = CommonDialogHelper.getInstance(
        content = CommonDialogModel(
            type = DialogStyleType.LOADING,
            content = CommonDialogContent(
                title = "프로필을 수정하시겠어요?".toSpannable()
            ),
            button = CommonButtonModel(
                buttonCount = ButtonCount.ZERO
            )
        )
    )

    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun initView() {
        super.initView()
        photoPicker = PhotoPicker(this)
        binding.fragment = this
        toolbarHelper = ToolbarHelper(
            data = ToolbarData(
                popStack = findNavController(),
                titleText = "프로필 설정",
                iconText = "완료",
                iconInteraction = this
            ),
            toolbar = binding.toolbar
        )
        toolbarHelper.set()

        profileViewModel.getUserProfile(getAccessTokenOrNull() ?: "")
        setNicknameTextWatcher()
    }

    override fun initObserver() {
        super.initObserver()
        // 프로필 조회
        profileViewModel.userProfile.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ModelState.Success -> {
                    val data = state.data

                    with(binding) {
                        Glide.with(requireContext()).load(data.profileUrl)
                            .error(R.drawable.ic_launcher_background)
                            .into(this.profileModifyImg)
                        profileNicknameInput.setText(data.nickName)
                    }
                }
                is ModelState.Error -> {}
                is ModelState.Loading -> {}
            }
        }

        // 프로필 업데이트
        profileViewModel.profileUpdateSuccess.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ModelState.Success -> {
                    if (loadingDialog.isAdded) {
                        loadingDialog.dismiss()
                    }
                }
                is ModelState.Error -> {
                    if (loadingDialog.isAdded) {
                        loadingDialog.dismiss()
                    }
                }
                is ModelState.Loading -> {
                    loadingDialog.show(childFragmentManager, "Dialog")
                }
            }
        }
    }

    private fun setNicknameTextWatcher() {
        binding.profileNicknameInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                profileViewModel.setUserNickname(p0?.toString() ?: "")
            }
        })
    }

    fun onClickProfileUpdate() {
        if (getPermissionManagerOrNull()?.checkPermissionAndMoveSettingIfDenied(
                PermissionManager.READ_MEDIA_PERMISSION,
                dialogTitle = getString(R.string.permission_manager_dialog_storage_title)
            ) != true
        ) return
        photoPicker?.invoke { uri ->
            val path = getAbsPath(uri = uri)
            if (path != null) {
                Glide.with(requireContext()).load(uri)
                    .error(R.drawable.ic_launcher_background)
                    .into(binding.profileModifyImg)
                profileViewModel.setProfileImg(path)
            }
        }
    }

    private fun getAbsPath(uri: Uri): String? {
        return try {
            val cursor = activity?.contentResolver?.query(
                uri, null, null, null, null
            )
            cursor?.run {
                moveToFirst()
                val index = getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                val ret = getString(index)
                close()
                ret
            }
        } catch (e: Exception) {
            Timber.e("$uri - $e")
            null
        }
    }

    override fun onToolbarIconClicked() {
        CommonDialogHelper.getInstance(
            content = CommonDialogModel(
                type = DialogStyleType.ALERT,
                content = CommonDialogContent(
                    title = "프로필을 수정하시겠어요?".toSpannable()
                ),
                button = CommonButtonModel(
                    buttonCount = ButtonCount.TWO,
                    positiveButtonText = "네, 수정할게요",
                    negativeButtonText = "아니요"
                )
            ),
            onConfirmListener = {
                object : OnButtonClickListener {
                    override fun setBusinessLogic() {
                        profileViewModel.updateUserProfile(getAccessTokenOrNull() ?: "")
                    }
                    override fun getReturnValue(value: Any) {}
                }
            }
        ).show(childFragmentManager, "Dialog")
    }
}