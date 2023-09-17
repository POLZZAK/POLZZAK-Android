package com.polzzak_android.presentation.feature.auth.signup

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.polzzak_android.R
import com.polzzak_android.presentation.common.util.getParcelableArrayListOrNull
import com.polzzak_android.presentation.common.util.getParcelableOrNull
import com.polzzak_android.common.util.livedata.EventWrapperObserver
import com.polzzak_android.presentation.common.util.loadCircleImageDrawableRes
import com.polzzak_android.presentation.common.util.loadCircleImageUrl
import com.polzzak_android.databinding.FragmentSignupBinding
import com.polzzak_android.presentation.feature.auth.model.MemberTypeDetail
import com.polzzak_android.presentation.feature.auth.model.SocialLoginType
import com.polzzak_android.presentation.feature.auth.signup.adapter.ParentTypeRollableAdapter
import com.polzzak_android.presentation.feature.auth.signup.model.NickNameUiModel
import com.polzzak_android.presentation.feature.auth.signup.model.NickNameValidationState
import com.polzzak_android.presentation.feature.auth.signup.model.SignUpPage
import com.polzzak_android.presentation.feature.auth.signup.model.SignUpTermsOfServiceModel
import com.polzzak_android.presentation.feature.root.MainViewModel
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.util.PermissionManager
import com.polzzak_android.presentation.common.util.getPermissionManagerOrNull
import com.polzzak_android.presentation.common.util.hideKeyboard
import com.polzzak_android.presentation.component.PolzzakSnackBar
import com.polzzak_android.presentation.component.errorOf
import com.polzzak_android.presentation.feature.term.TermDetailFragment
import com.polzzak_android.presentation.feature.term.model.TermType
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.sqrt

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignupBinding>() {
    override val layoutResId = R.layout.fragment_signup

    @Inject
    lateinit var signUpViewModelAssistedFactory: SignUpViewModel.SignUpAssistedFactory

    private val mainViewModel by viewModels<MainViewModel>(ownerProducer = { requireActivity() })

    private val signUpViewModel by viewModels<SignUpViewModel> {
        val userName = arguments?.getString(ARGUMENT_USER_ID_KEY, "")
        val socialType = arguments?.getParcelableOrNull(
            ARGUMENT_SOCIAL_LOGIN_TYPE_KEY,
            SocialLoginType::class.java
        )
        SignUpViewModel.provideFactory(signUpViewModelAssistedFactory, userName, socialType)
    }

    private val validNickNameRegex = Regex("""[0-9a-zA-z가-힣]{2,10}""")
    private var parentTypeRollableAdapter: ParentTypeRollableAdapter? = null

    private var photoPicker: PhotoPicker? = null
    override fun initView() {
        super.initView()
        photoPicker = PhotoPicker(this)
        binding.ivBtnBack.setOnClickListener {
            hideKeyboardAndClearFocus()
            if (signUpViewModel.pageLiveData.value is SignUpPage.TermsOfService) activity?.onBackPressedDispatcher?.onBackPressed()
            else signUpViewModel.movePrevPage()
        }
        binding.tvBtnNext.setOnClickListener {
            val pageData = signUpViewModel.pageLiveData.value ?: return@setOnClickListener
            when (pageData) {
                is SignUpPage.SetProfileImage -> signUpViewModel.requestSignUp()
                else -> signUpViewModel.moveNextPage()
            }
        }
        initSelectTypeView(binding = binding)
        initSelectParentTypeView(binding = binding)
        initSetNickNameView(binding = binding)
        initSelectProfileImageView(binding = binding)
        initTermsOfServiceView(binding = binding)
        addOnBackPressedDispatcher()
    }

    private fun addOnBackPressedDispatcher() {
        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val backToPrevPageList = listOf(
                    SignUpPage.SelectType::class.java,
                    SignUpPage.SelectParentType::class.java,
                    SignUpPage.SetNickName::class.java,
                    SignUpPage.SetProfileImage::class.java
                )
                val page = signUpViewModel.pageLiveData.value
                if (page != null && backToPrevPageList.contains(page.javaClass)) signUpViewModel.movePrevPage()
                else findNavController().popBackStack()
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(this, backPressedCallback)
    }

    private fun initSelectTypeView(binding: FragmentSignupBinding) {
        with(binding.inSelectType) {
            clSelectParentCard.setOnClickListener {
                signUpViewModel.selectTypeParent()
            }
            clSelectKidCard.setOnClickListener {
                signUpViewModel.selectTypeKid()
            }
        }
    }

    private fun initSelectParentTypeView(binding: FragmentSignupBinding) {
        val parentTypes = arguments?.getParcelableArrayListOrNull(
            ARGUMENT_PARENT_TYPES_KEY,
            MemberTypeDetail.Parent::class.java
        )?.toList() ?: emptyList()
        with(binding.inSelectParentType) {
            vpTypeCards.offscreenPageLimit = 2
            parentTypeRollableAdapter =
                ParentTypeRollableAdapter(parentTypes = listOf(null) + parentTypes)
            vpTypeCards.adapter = parentTypeRollableAdapter
            vpTypeCards.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    val selectedType = parentTypeRollableAdapter?.getSelectedType(position)
                    if (positionOffset == 0f) {
                        signUpViewModel.selectParentType(selectedTypeId = selectedType?.id)

                    } else {
                        signUpViewModel.selectParentType(selectedTypeId = null)
                    }
                }
            })
            val cardHeightPx =
                resources.getDimension(R.dimen.sign_up_select_parent_type_card_height)
            val vpHeightPx =
                resources.getDimension(R.dimen.sign_up_select_parent_type_view_pager_height)

            val secondItemRatio = 0.925f
            val thirdItemRatio = 0.8125f
            vpTypeCards.setPageTransformer { page, position ->
                val absPosition = abs(position)
                page.isVisible = absPosition < 3f
                var transY = 0f
                var scale = 1f
                var radius = cardHeightPx / 2
                page.translationZ = -absPosition
                val calYPosInCircle = { r: Float, xPos: Float ->
                    sqrt(r * r - (xPos * r).let { it * it })
                }
                val calScale = { ratio: Float, xPos: Float ->
                    1 - (1 - ratio) * xPos
                }
                transY += calYPosInCircle(radius, maxOf(0f, 1 - absPosition))
                scale *= calScale(secondItemRatio, minOf(1f, absPosition))
                if (absPosition > 1f) {
                    radius *= secondItemRatio
                    transY += calYPosInCircle(radius, 2 - absPosition)
                    scale *= calScale(thirdItemRatio, absPosition - 1)
                }
                page.translationY =
                    (if (position < 0) -transY else transY) - position * vpHeightPx
                page.scaleX = scale
                page.scaleY = scale
                page.isSelected =
                    (page.tag == ParentTypeRollableAdapter.ITEM_NORMAL_TAG && position == 0f)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initSetNickNameView(binding: FragmentSignupBinding) {
        with(binding.inSetNickName) {
            tvBtnCheckValidation.isEnabled = false
            ivBtnClearText.setOnClickListener {
                etInput.text.clear()
            }
            etInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    signUpViewModel.run {
                        setNickNameValue(p0?.toString() ?: "")
                    }
                }
            })
            etInput.setOnFocusChangeListener { _, isFocused ->
                val textUiModel = signUpViewModel.nickNameLiveData.value ?: NickNameUiModel()
                etInput.isSelected = true
                setNickNameResultTextView(isFocused = isFocused, uiModel = textUiModel)
            }
            tvBtnCheckValidation.setOnClickListener {
                signUpViewModel.requestCheckNickNameValidation()
                hideKeyboardAndClearFocus()
            }
            binding.root.setOnTouchListener { _, _ ->
                hideKeyboardAndClearFocus()
                etInput.clearFocus()
                false
            }
        }
    }

    private fun hideKeyboardAndClearFocus() {
        hideKeyboard()
        binding.inSetNickName.etInput.clearFocus()
    }

    private fun initSelectProfileImageView(binding: FragmentSignupBinding) {
        with(binding.inSelectProfileImage) {
            clBtnSelectPicture.setOnClickListener {
                if (getPermissionManagerOrNull()?.checkPermissionAndMoveSettingIfDenied(
                        PermissionManager.READ_MEDIA_PERMISSION,
                        dialogTitle = getString(R.string.permission_manager_dialog_storage_title)
                    ) != true
                ) return@setOnClickListener
                photoPicker?.invoke { uri ->
                    val path = getAbsPath(uri = uri)
                    signUpViewModel.setProfileImagePath(path = path)
                }
            }
        }
    }

    private fun initTermsOfServiceView(binding: FragmentSignupBinding) {
        with(binding.inTermsOfService) {
            tvBtnCheckAll.setOnClickListener {
                signUpViewModel.checkTermsOfService(clickModel = SignUpTermsOfServiceModel.ClickModel.ALL)
            }
            tvBtnCheckService.setOnClickListener {
                signUpViewModel.checkTermsOfService(clickModel = SignUpTermsOfServiceModel.ClickModel.SERVICE)
            }
            tvBtnCheckPrivacy.setOnClickListener {
                signUpViewModel.checkTermsOfService(clickModel = SignUpTermsOfServiceModel.ClickModel.PRIVACY)
            }
            ivBtnServiceDetail.setOnClickListener {
                val detailDataBundle = Bundle().apply {
                    putParcelable(TermDetailFragment.ARGUMENT_TYPE_KEY, TermType.SERVICE)
                }
                findNavController().navigate(
                    R.id.action_signUpFragment_to_termDetailFragment,
                    detailDataBundle
                )
            }
            ivBtnPrivacyDetail.setOnClickListener {
                val detailDataBundle = Bundle().apply {
                    putParcelable(TermDetailFragment.ARGUMENT_TYPE_KEY, TermType.PRIVACY)
                }
                findNavController().navigate(
                    R.id.action_signUpFragment_to_termDetailFragment,
                    detailDataBundle
                )
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

    override fun initObserver() {
        super.initObserver()
        observePageLiveData()
        observeMemberTypeLiveData()
        observeNickNameLiveData()
        observeProfileImageLiveData()
        observeTermsOfServiceLiveData()
        observeSignUpResultLiveData()
    }

    private fun observePageLiveData() {
        signUpViewModel.pageLiveData.observe(viewLifecycleOwner) {
            refreshNextButton()
            with(binding) {
                inSelectType.root.isVisible = (it is SignUpPage.SelectType)
                inSelectParentType.root.isVisible = (it is SignUpPage.SelectParentType)
                inSetNickName.root.isVisible = (it is SignUpPage.SetNickName)
                inSelectProfileImage.root.isVisible = (it is SignUpPage.SetProfileImage)
                inTermsOfService.root.isVisible = (it is SignUpPage.TermsOfService)
                cpvProgressView.isVisible = (it.progressCount != null)
                cpvProgressView.checkedCount = it.progressCount ?: 0
                cpvProgressView.maxCount = it.maxCount
                when (it) {
                    is SignUpPage.SelectParentType -> {
                        val currentTypeId = signUpViewModel.memberTypeLiveData.value?.selectedTypeId
                        val adapterStartPosition =
                            parentTypeRollableAdapter?.getTypeStartPosition(parentTypeId = currentTypeId)
                        adapterStartPosition?.let {
                            inSelectParentType.vpTypeCards.setCurrentItem(
                                it,
                                false
                            )
                        }
                    }

                    is SignUpPage.SetNickName -> {
                        inSetNickName.etInput.setText(signUpViewModel.nickNameLiveData.value?.nickName)
                    }

                    else -> {
                        //do nothing
                    }
                }

            }
        }
    }

    private fun observeMemberTypeLiveData() {
        signUpViewModel.memberTypeLiveData.observe(viewLifecycleOwner) {
            with(binding.inSelectType) {
                clSelectParentCard.isSelected = (it.isParent())
                clSelectKidCard.isSelected = (it.isKid())
            }
            refreshNextButton()
        }

    }

    private fun observeNickNameLiveData() {
        signUpViewModel.nickNameLiveData.observe(viewLifecycleOwner) {
            with(binding.inSetNickName) {
                tvBtnCheckValidation.isEnabled =
                    validNickNameRegex.matches(it.nickName ?: "")
                tvBtnCheckValidation.text =
                    getString(if (it.nickNameState is NickNameValidationState.Valid) R.string.signup_nickname_check_validation_btn_complete else R.string.signup_nickname_check_validation_btn_check)
                setNickNameResultTextView(isFocused = etInput.isFocused, uiModel = it)
                ivBtnClearText.isVisible = !it.nickName.isNullOrEmpty() && etInput.isFocused
                refreshNextButton()
                if (it.nickNameState is NickNameValidationState.Error) PolzzakSnackBar.errorOf(
                    binding.root,
                    it.nickNameState.exception
                ).show()
            }
        }
    }

    private fun observeProfileImageLiveData() {
        signUpViewModel.profileImageLiveData.observe(viewLifecycleOwner) {
            with(binding.inSelectProfileImage) {
                it.path?.let { path ->
                    ivImage.loadCircleImageUrl(imageUrl = path)
                } ?: run {
                    ivImage.loadCircleImageDrawableRes(drawableRes = R.drawable.ic_launcher_background)
                }
            }
        }
    }

    private fun observeTermsOfServiceLiveData() {
        signUpViewModel.termsOfServiceLiveData.observe(viewLifecycleOwner) {
            with(binding.inTermsOfService) {
                tvBtnCheckAll.isSelected = (it.isCheckedService && it.isCheckedPrivacy)
                tvBtnCheckService.isSelected = it.isCheckedService
                tvBtnCheckPrivacy.isSelected = it.isCheckedPrivacy
                refreshNextButton()
            }
        }
    }

    private fun observeSignUpResultLiveData() {
        signUpViewModel.signUpResultLiveData.observe(viewLifecycleOwner, EventWrapperObserver {
            var btnNextEnabled = true
            var btnTextRes = R.string.signup_complete
            when (it) {
                is ModelState.Loading -> {
                    btnNextEnabled = false
                    btnTextRes = R.string.signup_loading
                }

                is ModelState.Success -> {
                    mainViewModel.accessToken = it.data.accessToken
                    val action = when (it.data.memberTypeId) {
                        MemberTypeDetail.KID_TYPE_ID -> R.id.action_signUpFragment_to_kidOnBoardingFragment
                        else -> R.id.action_signUpFragment_to_protectorOnBoardingFragment
                    }
                    findNavController().navigate(action)
                }

                is ModelState.Error -> {
                    PolzzakSnackBar.errorOf(binding.root, it.exception).show()
                }
            }
            binding.tvBtnNext.run {
                text = getString(btnTextRes)
                isEnabled = btnNextEnabled
            }
        })
    }

    private fun setNickNameResultTextView(isFocused: Boolean, uiModel: NickNameUiModel) {
        with(binding.inSetNickName) {
            tvCheckDuplicatedResult.text = createDuplicatedResultText(
                isFocused = isFocused,
                isSelected = etInput.isSelected,
                uiModel = uiModel
            )
            val textColor = ContextCompat.getColor(
                binding.root.context,
                if (uiModel.nickNameState is NickNameValidationState.Valid) R.color.primary else R.color.error_500
            )
            tvCheckDuplicatedResult.setTextColor(textColor)
            val resultDrawableRes =
                if (uiModel.nickNameState is NickNameValidationState.Valid) R.drawable.ic_signup_nickname_check_result_valid else 0
            tvCheckDuplicatedResult.setCompoundDrawablesWithIntrinsicBounds(
                resultDrawableRes,
                0,
                0,
                0
            )
            val lengthText = if (isFocused) "${(uiModel.nickName ?: "").length}/10" else ""
            tvInputLength.text = lengthText
            etInput.setBackgroundResource(
                createDuplicatedEditTextBackgroundResId(
                    isFocused = isFocused,
                    isSelected = etInput.isSelected,
                    uiModel = uiModel
                )
            )
        }
    }

    private fun createDuplicatedResultText(
        isFocused: Boolean,
        isSelected: Boolean,
        uiModel: NickNameUiModel
    ): String {
        return when {
            !isSelected -> ""
            uiModel.nickNameState is NickNameValidationState.Invalid -> getString(R.string.nickname_check_validation_duplicated_nickname)
            uiModel.nickNameState is NickNameValidationState.Valid -> getString(R.string.nickname_check_validation_possible)
            uiModel.nickName == null -> ""
            uiModel.nickName.isEmpty() -> if (!uiModel.isEdited) "" else getString(R.string.nickname_check_validation_under_minimum_length)
            uiModel.nickName.length < 2 -> getString(R.string.nickname_check_validation_under_minimum_length)
            uiModel.nickName.length > 10 -> getString(R.string.nickname_check_validation_over_maximum_length)
            validNickNameRegex.matches(uiModel.nickName) -> ""
            else -> getString(R.string.nickname_check_validation_invalid_char)
        }
    }

    private fun createDuplicatedEditTextBackgroundResId(
        isFocused: Boolean,
        isSelected: Boolean,
        uiModel: NickNameUiModel
    ): Int {
        return when {
            !isSelected -> R.drawable.shape_rectangle_white_stroke_gray_300_r8
            uiModel.nickNameState is NickNameValidationState.Invalid -> R.drawable.shape_rectangle_white_stroke_error_500_r8
            uiModel.nickNameState is NickNameValidationState.Valid -> R.drawable.shape_rectangle_white_stroke_primary_r8
            uiModel.nickName == null -> R.drawable.shape_rectangle_white_stroke_gray_300_r8
            uiModel.nickName.isEmpty() -> if (!uiModel.isEdited) {
                if (isFocused) R.drawable.shape_rectangle_white_stroke_primary_r8 else R.drawable.shape_rectangle_white_stroke_gray_300_r8
            } else R.drawable.shape_rectangle_white_stroke_error_500_r8

            uiModel.nickName.length < 2 -> R.drawable.shape_rectangle_white_stroke_error_500_r8
            uiModel.nickName.length > 10 -> R.drawable.shape_rectangle_white_stroke_error_500_r8
            validNickNameRegex.matches(uiModel.nickName) -> R.drawable.shape_rectangle_white_stroke_primary_r8
            else -> R.drawable.shape_rectangle_white_stroke_error_500_r8
        }
    }

    private fun refreshNextButton() {
        val memberTypeData = signUpViewModel.memberTypeLiveData.value
        val nickNameData = signUpViewModel.nickNameLiveData.value
        val termsOfServiceData = signUpViewModel.termsOfServiceLiveData.value
        val nextBtnStringRes = when (signUpViewModel.pageLiveData.value) {
            is SignUpPage.SetProfileImage -> R.string.signup_complete
            else -> R.string.common_next
        }
        binding.tvBtnNext.text = getString(nextBtnStringRes)
        binding.tvBtnNext.isEnabled =
            when (signUpViewModel.pageLiveData.value) {
                is SignUpPage.SelectType -> memberTypeData?.selectedType != null
                is SignUpPage.SelectParentType -> memberTypeData?.selectedTypeId != null
                is SignUpPage.SetNickName -> nickNameData?.nickNameState is NickNameValidationState.Valid
                is SignUpPage.SetProfileImage -> true
                is SignUpPage.TermsOfService -> {
                    termsOfServiceData?.run { isCheckedPrivacy && isCheckedService } ?: false
                }

                else -> false
            }
    }


    companion object {
        const val ARGUMENT_USER_ID_KEY = "argument_user_id_key"
        const val ARGUMENT_SOCIAL_LOGIN_TYPE_KEY = "argument_social_login_type_key"
        const val ARGUMENT_PARENT_TYPES_KEY = "argument_parent_types_key"
    }
}