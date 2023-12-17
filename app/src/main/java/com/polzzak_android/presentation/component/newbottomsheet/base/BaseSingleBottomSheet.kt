package com.polzzak_android.presentation.component.newbottomsheet.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseSingleBottomSheet<VB : ViewDataBinding> : BaseBottomSheet() {

    private var _binding: VB? = null
    protected val binding
        get() = _binding!!

    @get:LayoutRes
    protected abstract val layoutId: Int

    protected abstract fun initialize()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        _binding = DataBindingUtil.inflate(
            layoutInflater,
            layoutId,
            container,
            false
        )

        baseBinding.apply {
            fragmentContainer.visibility = View.GONE
            layoutFrame.addView(binding.root)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialize()
    }

    override fun onDestroyView() {
        _binding = null

        super.onDestroyView()
    }
}