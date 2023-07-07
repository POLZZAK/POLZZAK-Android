package com.polzzak_android.presentation.link.management.base

import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentLinkManagementBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.link.management.model.LinkManagementHomeTabTypeModel
import com.polzzak_android.presentation.link.model.LinkMemberType

abstract class BaseLinkManagementFragment : BaseFragment<FragmentLinkManagementBinding>() {
    override val layoutResId: Int = R.layout.fragment_link_management

    protected abstract val targetLinkMemberType: LinkMemberType
    protected abstract val linkMemberType: LinkMemberType

    private val targetLinkTypeStringOrEmpty
        get() = context?.getString(targetLinkMemberType.stringRes) ?: ""
    private val linkMemberTypeStringOrEmpty
        get() = context?.getString(linkMemberType.stringRes) ?: ""

    override fun initView() {
        super.initView()
        with(binding) {

        }
        initHomeView()
    }

    private fun initHomeView() {
        with(binding.inHome) {
            val tabs = listOf(
                LinkManagementHomeTabTypeModel.LINK,
                LinkManagementHomeTabTypeModel.RECEIVED,
                LinkManagementHomeTabTypeModel.SENT
            )
            tabs.forEach {
                tlTab.addTab(tlTab.newTab().setText(it.titleStringRes))
            }
            tlTab.addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val position = tab?.position ?: return

                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })
        }
    }
}