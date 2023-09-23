package com.polzzak_android.presentation.feature.notification.setting

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.polzzak_android.R
import com.polzzak_android.common.util.livedata.EventWrapperObserver
import com.polzzak_android.databinding.FragmentNotificationSettingBinding
import com.polzzak_android.databinding.ItemNotificationSettingBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.util.PermissionManager
import com.polzzak_android.presentation.common.util.getAccessTokenOrNull
import com.polzzak_android.presentation.common.util.getPermissionManagerOrNull
import com.polzzak_android.presentation.component.PolzzakSnackBar
import com.polzzak_android.presentation.component.errorOf
import com.polzzak_android.presentation.component.toolbar.ToolbarData
import com.polzzak_android.presentation.component.toolbar.ToolbarHelper
import com.polzzak_android.presentation.feature.notification.setting.model.SettingMenuType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationSettingFragment :
    BaseFragment<FragmentNotificationSettingBinding>() {
    override val layoutResId: Int = R.layout.fragment_notification_setting

    private val notificationSettingViewModel by viewModels<NotificationSettingViewModel>()

    private val menuToBindingMap = HashMap<SettingMenuType, ItemNotificationSettingBinding>()

    private val switchTouchListener = { onGranted: (View?) -> Unit ->
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

    override fun initView() {
        super.initView()
        initToolbar()
        initAllMenu()
        notificationSettingViewModel.requestSettingMenu(accessToken = getAccessTokenOrNull() ?: "")
    }

    private fun initToolbar() {
        ToolbarHelper(
            data = ToolbarData(
                popStack = findNavController(),
                titleText = getString(R.string.notification_setting_header_title),
            ), toolbar = binding.inToolbar
        ).set()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initAllMenu() {
        with(binding) {
            llSettings.isVisible = false
            scALlMenuSwitch.setOnTouchListener(switchTouchListener { view ->
                (view as? SwitchCompat)?.let {
                    notificationSettingViewModel.checkAllMenu(
                        accessToken = getAccessTokenOrNull() ?: "", isChecked = !it.isChecked
                    )
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        notificationSettingViewModel.setPermissionGranted(isGranted = isNotificationPermissionGranted())
    }

    private fun isNotificationPermissionGranted() = PermissionManager.NOTIFICATION_PERMISSION?.let {
        getPermissionManagerOrNull()?.isPermissionsGranted(it) ?: false
    } ?: true

    override fun initObserver() {
        super.initObserver()
        observeSettingMenus()
        observeErrorEvent()
    }

    private fun observeSettingMenus() {
        notificationSettingViewModel.settingMenusLiveData.observe(viewLifecycleOwner) {
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
                        menuToBindingMap.getOrPut(menuModel.key) {
                            createSettingMenuView(menuType = menuModel.key)
                        }.run {
                            scSwitch.isChecked = isGranted && menuModel.value
                        }
                    }
                    binding.scALlMenuSwitch.isChecked = isGranted && menusModel.isAllMenuChecked
                }

                is ModelState.Error -> {
                    isSettingsVisible = false
                }
            }

            binding.inLoading.root.isVisible = isLoadingVisible
            binding.llSettings.isVisible = isSettingsVisible
        }
    }

    private fun observeErrorEvent() {
        notificationSettingViewModel.errorEventLiveData.observe(
            viewLifecycleOwner,
            EventWrapperObserver {
                PolzzakSnackBar.errorOf(binding.root, it).show()
            })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun createSettingMenuView(menuType: SettingMenuType) =
        ItemNotificationSettingBinding.inflate(LayoutInflater.from(binding.root.context))
            .also { settingBinding ->
                binding.llSettings.addView(settingBinding.root)
                settingBinding.tvTitle.text =
                    getString(menuType.titleStringRes)
                settingBinding.tvContent.text =
                    getString(menuType.contentStringRes)
                settingBinding.scSwitch.setOnTouchListener(switchTouchListener { view ->
                    (view as? SwitchCompat)?.let {
                        notificationSettingViewModel.checkMenu(
                            accessToken = getAccessTokenOrNull() ?: "",
                            type = menuType,
                            isChecked = !it.isChecked
                        )
                    }
                })
            }

    override fun onDestroyView() {
        super.onDestroyView()
        menuToBindingMap.clear()
    }
}