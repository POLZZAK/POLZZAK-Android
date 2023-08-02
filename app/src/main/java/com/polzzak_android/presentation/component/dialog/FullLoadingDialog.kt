package com.polzzak_android.presentation.component.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.polzzak_android.R
import com.polzzak_android.databinding.DialogFullLoadingOpacityBinding

class FullLoadingDialog : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return DataBindingUtil.inflate<DialogFullLoadingOpacityBinding>(
            inflater,
            R.layout.dialog_full_loading_opacity,
            container,
            false
        ).root
    }
}