package com.polzzak_android.presentation.signup

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.polzzak_android.R
import com.polzzak_android.common.base.BaseFragment
import com.polzzak_android.common.model.MemberType
import com.polzzak_android.common.model.SocialLoginType
import com.polzzak_android.databinding.FragmentSignupBinding
import com.polzzak_android.presentation.signup.adapter.SignUpSelectParentTypeAdapter
import com.polzzak_android.presentation.signup.model.NickNameUiModel
import com.polzzak_android.presentation.signup.model.NickNameValidationState
import com.polzzak_android.presentation.signup.model.SignUpPage
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.abs

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignupBinding>() {
    override val layoutResId = R.layout.fragment_signup
    private val validNickNameRegex = Regex("""[0-9a-zA-z]{2,10}""")

    @Inject
    lateinit var signUpViewModelAssistedFactory: SignUpViewModel.SignUpAssistedFactory
    private val signUpViewModel by viewModels<SignUpViewModel> {
        val userName = arguments?.getString(ARGUMENT_USER_ID_KEY, "")
        val socialType = arguments?.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getSerializable(ARGUMENT_SOCIAL_LOGIN_TYPE_KEY, SocialLoginType::class.java)
            } else {
                @Suppress("Deprecation")
                getSerializable(ARGUMENT_SOCIAL_LOGIN_TYPE_KEY) as? SocialLoginType
            }
        }
        SignUpViewModel.provideFactory(signUpViewModelAssistedFactory, userName, socialType)
    }

    override fun initView() {
        super.initView()
        binding.ivBtnBack.setOnClickListener {
            hideKeyboard()
            signUpViewModel.movePrevPage()
        }
        initSelectTypeView(binding = binding)
        initSelectParentTypeView(binding = binding)
        initSetNickNameView(binding = binding)
        addOnBackPressedDispatcher()
    }

    private fun addOnBackPressedDispatcher() {
        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val backToPrevPageList = listOf(
                    SignUpPage.SELECT_PARENT_TYPE,
                    SignUpPage.SET_NICKNAME,
                    SignUpPage.SET_PROFILE_IMAGE
                )
                val page = signUpViewModel.pageLiveData.value
                if (backToPrevPageList.contains(page)) signUpViewModel.movePrevPage()
                else findNavController().popBackStack()
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(this, backPressedCallback)
    }

    private fun initSelectTypeView(binding: FragmentSignupBinding) {
        with(binding.inSelectType) {
            tvBtnSelectParent.setOnClickListener {
                signUpViewModel.selectTypeParent()
            }
            tvBtnSelectKid.setOnClickListener {
                signUpViewModel.selectTypeKid()
            }
            tvBtnAccept.isEnabled = false
            tvBtnAccept.setOnClickListener {
                signUpViewModel.moveNextPage()
            }
        }
    }

    private fun initSelectParentTypeView(binding: FragmentSignupBinding) {
        with(binding.inSelectParentType) {
            vpTypeCards.offscreenPageLimit = 3
            vpTypeCards.adapter = SignUpSelectParentTypeAdapter()
            vpTypeCards.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    signUpViewModel.selectParentType(
                        SignUpSelectParentTypeAdapter.getSelectedType(
                            position
                        )
                    )
                }
            })
            //TODO select parent type 구현
            vpTypeCards.setPageTransformer { page, position ->
                Timber.d("${page} $position")
                page.translationY = -position * 300f
                page.translationZ = 1 - abs(position)
            }

            tvBtnAccept.isEnabled = false
            tvBtnAccept.setOnClickListener {
                signUpViewModel.moveNextPage()
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
                        cancelCheckNickNameValidationJob()
                        setNickNameValue(p0?.toString() ?: "")
                    }
                }
            })
            etInput.setOnFocusChangeListener { _, isFocused ->
                val textUiModel = signUpViewModel.nickNameLiveData.value ?: NickNameUiModel()
                if (textUiModel.nickName == null) signUpViewModel.setNickNameValue("")
                else setNickNameResultTextView(isFocused = isFocused, uiModel = textUiModel)
            }
            tvBtnAccept.isEnabled = false
            tvBtnAccept.setOnClickListener {
                signUpViewModel.moveNextPage()
            }
            tvBtnCheckValidation.setOnClickListener {
                signUpViewModel.requestCheckNickNameValidation()
            }
            binding.root.setOnTouchListener { _, _ ->
                hideKeyboard()
                etInput.clearFocus()
                false
            }
        }
    }

    private fun hideKeyboard() {
        activity?.run {
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            currentFocus?.let { currentFocus ->
                inputManager?.hideSoftInputFromWindow(
                    currentFocus.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }
    }

    override fun initObserver() {
        super.initObserver()
        signUpViewModel.pageLiveData.observe(viewLifecycleOwner) {
            with(binding) {
                inSelectType.root.isVisible = false
                inSelectParentType.root.isVisible = false
                inSetNickName.root.isVisible = false
                inSelectProfileImage.root.isVisible = false
                ivBtnBack.isVisible = (it != SignUpPage.SELECT_TYPE && it != SignUpPage.ERROR)
                when (it) {
                    SignUpPage.SELECT_TYPE -> {
                        inSelectType.root.isVisible = true
                    }

                    SignUpPage.SELECT_PARENT_TYPE -> {
                        val currentType = signUpViewModel.memberTypeLiveData.value?.type
                        val currentPosition =
                            SignUpSelectParentTypeAdapter.getTypePosition(currentType as? MemberType.Parent)
                        inSelectParentType.vpTypeCards.setCurrentItem(currentPosition, false)
                        inSelectParentType.root.isVisible = true
                    }

                    SignUpPage.SET_NICKNAME -> {
                        inSetNickName.root.isVisible = true
                    }

                    SignUpPage.SET_PROFILE_IMAGE -> {
                        inSelectProfileImage.root.isVisible = true
                    }

                    else -> {}
                }

            }
        }

        signUpViewModel.memberTypeLiveData.observe(viewLifecycleOwner) {
            binding.inSelectType.tvBtnAccept.isEnabled = (it.isParentType != null)
            binding.inSelectParentType.tvBtnAccept.isEnabled =
                ((it.isParentType == true) && (it.type is MemberType.Parent))
        }

        signUpViewModel.nickNameLiveData.observe(viewLifecycleOwner) {
            with(binding.inSetNickName) {
                tvBtnCheckValidation.isEnabled = validNickNameRegex.matches(it.nickName ?: "")
                //TODO string resource로 변경
                tvBtnCheckValidation.text =
                    if (it.nickNameState == NickNameValidationState.VALID) "확인 완료" else "중복 확인"
                setNickNameResultTextView(isFocused = etInput.isFocused, uiModel = it)
                ivBtnClearText.isVisible = !it.nickName.isNullOrEmpty() && etInput.isFocused
                tvBtnAccept.isEnabled = (it.nickNameState == NickNameValidationState.VALID)
            }
        }
    }

    private fun setNickNameResultTextView(isFocused: Boolean, uiModel: NickNameUiModel) {
        with(binding.inSetNickName) {
            tvCheckDuplicatedResult.text = createDuplicatedResultText(
                isFocused = isFocused,
                uiModel = uiModel
            )
            val textColor = ContextCompat.getColor(
                binding.root.context,
                if (uiModel.nickNameState == NickNameValidationState.VALID) R.color.primary else R.color.error_500
            )
            tvCheckDuplicatedResult.setTextColor(textColor)
            val lengthText = if (isFocused) "${(uiModel.nickName ?: "").length}/10" else ""
            tvInputLength.text = lengthText
        }
    }

    //TODO string resource로 변경
    private fun createDuplicatedResultText(
        isFocused: Boolean,
        uiModel: NickNameUiModel
    ): String {
        return when {
            uiModel.nickNameState == NickNameValidationState.INVALID -> "이미 사용되고 있는 닉네임이에요"
            uiModel.nickNameState == NickNameValidationState.VALID -> "사용 가능한 닉네임이에요"
            uiModel.nickName == null -> ""
            uiModel.nickName.isEmpty() -> if (isFocused) "" else "최소 2글자로 설정해주세요"
            uiModel.nickName.length < 2 -> "최소 2글자로 설정해주세요"
            uiModel.nickName.length > 10 -> "10자까지만 쓸 수 있어요"
            validNickNameRegex.matches(uiModel.nickName) -> ""
            else -> "특수문자(공백)는 쓸 수 없어요"
        }
    }

    companion object {
        const val ARGUMENT_USER_ID_KEY = "argument_user_id_key"
        const val ARGUMENT_SOCIAL_LOGIN_TYPE_KEY = "argument_social_login_type_key"
    }
}