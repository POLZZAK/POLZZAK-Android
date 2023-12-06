package com.polzzak_android.presentation.component.newbottomsheet.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

/**
 * 바텀시트에 올라가는 Fragment의 Base가 되는 클래스
 */
abstract class BaseSheetFragment<VB : ViewDataBinding> : Fragment() {
    private var _binding: VB? = null
    protected val binding
        get() = _binding!!

    @get:LayoutRes
    protected abstract val layoutId: Int

    abstract fun initialize()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = DataBindingUtil.inflate(
            layoutInflater,
            layoutId,
            container,
            false
        )

        return binding.root
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