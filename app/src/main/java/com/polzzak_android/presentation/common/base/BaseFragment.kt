package com.polzzak_android.presentation.common.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.polzzak_android.presentation.feature.root.MainActivity

abstract class BaseFragment<B : ViewDataBinding> : Fragment() {
    @get:LayoutRes
    protected abstract val layoutResId: Int
    private var _binding: B? = null
    protected val binding get() = _binding!!
    override fun onAttach(context: Context) {
        (activity as? MainActivity)?.resetBackPressedEvent()
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = viewLifecycleOwner
        initView()
        setToolbar()
        initObserver()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected open fun setToolbar() {}
    protected open fun initView() {}
    protected open fun initObserver() {}
}