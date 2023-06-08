package com.polzzak_android.presentation.search.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.polzzak_android.databinding.FragmentSearchBinding

abstract class BaseSearchDialogFragment : DialogFragment() {
    private var _binding: FragmentSearchBinding? = null
    protected val binding get() = _binding!!

    abstract val targetString: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO string resource로 변경
        binding.tvTitle.text = "$targetString 찾기"
        binding.etSearch.hint = "$targetString 검색"
        isCancelable = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}