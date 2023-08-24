package com.polzzak_android.presentation.feature.notification.setting.base

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentNotificationSettingBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.common.util.BindableItemAdapter
import com.polzzak_android.presentation.feature.notification.NotificationViewModel
import com.polzzak_android.presentation.feature.notification.setting.item.NotificationSettingAllItem
import com.polzzak_android.presentation.feature.notification.setting.item.NotificationSettingItem
import com.polzzak_android.presentation.feature.notification.setting.model.SettingMenuModel
import com.polzzak_android.presentation.feature.notification.setting.model.SettingMenuType

//TODO 하단 툴바 숨김
class NotificationSettingFragment :
    BaseFragment<FragmentNotificationSettingBinding>() {
    override val layoutResId: Int = R.layout.fragment_notification_setting

    private val notificationViewModel by viewModels<NotificationViewModel>(ownerProducer = {
        parentFragment ?: this@NotificationSettingFragment
    })

    override fun initView() {
        super.initView()
        with(binding) {
            ivHeaderBtnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
        initRecyclerView()
        notificationViewModel.requestSettingMenu()
    }

    private fun initRecyclerView() {
        with(binding) {
            rvSetting.layoutManager = LinearLayoutManager(requireContext())
            rvSetting.adapter = BindableItemAdapter()
        }
    }

    override fun initObserver() {
        super.initObserver()
        notificationViewModel.settingMenusLiveData.observe(viewLifecycleOwner) {
            val adapter = (binding.rvSetting.adapter as? BindableItemAdapter) ?: return@observe
            adapter.updateItem(item = createSettingMenuItem(model = it))
        }
    }

    private fun createSettingMenuItem(model: List<SettingMenuModel>): List<BindableItem<*>> =
        model.map {
            when (it.type) {
                is SettingMenuType.All -> NotificationSettingAllItem(model = it.type)
                is SettingMenuType.Menu -> NotificationSettingItem(model = it.type)
            }
        }
}