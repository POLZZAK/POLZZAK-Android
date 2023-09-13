package com.polzzak_android.presentation.feature.notification.setting.base

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentNotificationSettingBinding
import com.polzzak_android.databinding.ItemNotificationSettingBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.util.PermissionManager
import com.polzzak_android.presentation.common.util.getPermissionManagerOrNull
import com.polzzak_android.presentation.feature.notification.NotificationViewModel
import com.polzzak_android.presentation.feature.notification.setting.model.SettingMenuType

abstract class BaseNotificationSettingFragment :
    BaseFragment<FragmentNotificationSettingBinding>() {
    override val layoutResId: Int = R.layout.fragment_notification_setting

    private val notificationViewModel by viewModels<NotificationViewModel>(ownerProducer = {
        parentFragment ?: this@BaseNotificationSettingFragment
    })

    abstract val menuTypes: List<SettingMenuType>

    private val menuToBindingMap = HashMap<SettingMenuType, ItemNotificationSettingBinding>()

    override fun initView() {
        super.initView()
        initMenusView()
        with(binding) {
            ivHeaderBtnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
        notificationViewModel.requestSettingMenu()
    }

    override fun onResume() {
        super.onResume()
        notificationViewModel.setPermissionGranted(isGranted = isNotificationPermissionGranted())
    }

    private fun isNotificationPermissionGranted() = PermissionManager.NOTIFICATION_PERMISSION?.let {
        getPermissionManagerOrNull()?.isPermissionsGranted(it) ?: false
    } ?: true

    override fun initObserver() {
        super.initObserver()
        notificationViewModel.settingMenusLiveData.observe(viewLifecycleOwner) {
            var isLoadingVisible = false
            var isSettingsVisible = false
            when (it) {
                is ModelState.Loading -> {
                    isLoadingVisible = true
                }

                is ModelState.Success -> {
                    val menusModel = it.data.menusModel ?: return@observe
                    val isGranted = it.data.isGranted ?: return@observe
                    isSettingsVisible = true
                    menusModel.typeToCheckedMap.forEach { menuModel ->
                        menuToBindingMap[menuModel.key]?.scSwitch?.run {
                            isChecked = isGranted && menuModel.value
                        }
                    }
                    binding.scALlMenuSwitch.isChecked = isGranted && menusModel.isAllMenuChecked
                }

                is ModelState.Error -> {
                    //TODO error 스낵바
                }
            }

            binding.inLoading.root.isVisible = isLoadingVisible
            binding.llSettings.isVisible = isSettingsVisible
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initMenusView() {
        val switchTouchListener = { onGranted: (View?) -> Unit ->
            object : View.OnTouchListener {
                override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {
                    if (motionEvent?.action == MotionEvent.ACTION_UP) {
                        if (isNotificationPermissionGranted() || PermissionManager.NOTIFICATION_PERMISSION == null) onGranted.invoke(
                            view
                        )
                        else getPermissionManagerOrNull()?.checkPermissionAndMoveSettingIfDenied(
                            PermissionManager.NOTIFICATION_PERMISSION,
                            dialogTitle = getString(R.string.permission_manager_dialog_notification_title)
                        )
                        return true
                    }
                    return false
                }
            }
        }
        with(binding) {
            llSettings.isVisible = false
            scALlMenuSwitch.setOnTouchListener(switchTouchListener { view ->
                (view as? SwitchCompat)?.let {
                    notificationViewModel.checkAllMenu(!it.isChecked)
                }
            })
        }

        menuTypes.forEachIndexed { idx, type ->
            val settingBinding =
                ItemNotificationSettingBinding.inflate(LayoutInflater.from(binding.root.context))
                    .apply {
                        tvTitle.text = getString(type.titleStringRes)
                        tvContent.text = getString(type.contentStringRes)
                        vDivider.isVisible = (idx < menuTypes.lastIndex)
                        scSwitch.setOnTouchListener(switchTouchListener { view ->
                            (view as? SwitchCompat)?.let {
                                notificationViewModel.checkMenu(
                                    type = type,
                                    isChecked = !it.isChecked
                                )
                            }
                        })
                    }
            menuToBindingMap[type] = settingBinding
            binding.llSettings.addView(settingBinding.root)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        menuToBindingMap.clear()
    }
}