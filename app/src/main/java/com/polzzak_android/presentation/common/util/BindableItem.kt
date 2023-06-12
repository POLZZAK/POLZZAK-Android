package com.polzzak_android.presentation.common.util

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding

/**
 * BindItemAdapter에 들어가는 Item class
 * 아래 추상클래스를 상속해서 구현
 */
abstract class BindableItem<B : ViewDataBinding> {
    @get:LayoutRes
    abstract val layoutRes: Int

    open fun onCreateViewHolder(parent: ViewGroup, position: Int) {}

    abstract fun bind(binding: B, position: Int)

    abstract fun areItemsTheSame(other: BindableItem<*>): Boolean
    abstract fun areContentsTheSame(other: BindableItem<*>): Boolean
}