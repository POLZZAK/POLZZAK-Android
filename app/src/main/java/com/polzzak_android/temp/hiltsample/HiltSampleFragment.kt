package com.polzzak_android.temp.hiltsample

import androidx.fragment.app.viewModels
import com.polzzak_android.R
import com.polzzak_android.common.base.BaseFragment
import com.polzzak_android.databinding.FragmentHiltSampleBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HiltSampleFragment : BaseFragment<FragmentHiltSampleBinding>() {
    override val layoutResId = R.layout.fragment_hilt_sample

    private val viewModel by viewModels<HiltSampleViewModel>()

    override fun initView() {
        super.initView()
        initObserver()
        viewModel.fetchSampleData()
    }

    private fun initObserver() {
        viewModel.sampleLiveData.observe(viewLifecycleOwner) {
            binding.tvTest.text = "$it"
        }
    }
}