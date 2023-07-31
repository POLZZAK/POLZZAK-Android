package com.polzzak_android.presentation.component.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.polzzak_android.databinding.FragmentSelectUserFilterBinding
import com.polzzak_android.presentation.common.adapter.MainSelectUserAdapter
import com.polzzak_android.presentation.feature.stamp.intercation.MainSelectUserInteraction

class SelectUserFilterFragment : BottomSheetDialogFragment(), MainSelectUserInteraction {

    // Todo: base 필요?
    private var _binding: FragmentSelectUserFilterBinding? = null
    private val binding get() = _binding!!

    private lateinit var rvAdapter: MainSelectUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectUserFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dummy = listOf<String>(
            "전체",
            "쿼카",
            "멜론수박",
            "아이유",
            "test1"
        )

        rvAdapter = MainSelectUserAdapter(dummy, this)
        binding.userSelectModalRc.adapter = rvAdapter
        binding.userSelectModalRc.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): SelectUserFilterFragment {
            return SelectUserFilterFragment()
        }
    }

    override fun onUserClicked(userItem: String) {
        Toast.makeText(context, "$userItem 클릭", Toast.LENGTH_SHORT).show()
    }
}