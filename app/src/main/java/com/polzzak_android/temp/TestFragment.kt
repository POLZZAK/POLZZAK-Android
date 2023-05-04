package com.polzzak_android.temp

import com.polzzak_android.R
import com.polzzak_android.common.base.BaseFragment
import com.polzzak_android.databinding.FragmentTestBinding

//TODO BaseFragment, hilt test용 나중에 삭제 필요
class TestFragment(override val layoutResId: Int = R.layout.fragment_test) :
    BaseFragment<FragmentTestBinding>() {

    override fun initView() {
        super.initView()
        binding.tvTest.text = "_test fragment"
    }
}