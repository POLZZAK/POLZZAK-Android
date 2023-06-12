package com.polzzak_android.presentation.common.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

/**
 * ItemViewType 구분 필요X
 * DiffUtil 기반 Adapter
 * BindableItem을 상속받은 class들로 update
 */
class BindableItemAdapter :
    RecyclerView.Adapter<BindableItemAdapter.BindableViewHolder<out ViewDataBinding>>() {
    private val asyncDiffer = AsyncListDiffer(this, DiffUtilCallback())
    val currentList: List<BindableItem<*>> get() = asyncDiffer.currentList

    fun updateItem(item: List<BindableItem<*>>) {
        asyncDiffer.submitList(item)
    }

    fun addItem(items: List<BindableItem<*>>) {
        val newItems = currentList + items
        asyncDiffer.submitList(newItems)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BindableViewHolder<out ViewDataBinding> {
        val item = currentList[viewType]
        val layoutInflater = LayoutInflater.from(parent.context)

        return BindableViewHolder(
            item = item,
            binding = DataBindingUtil.inflate(layoutInflater, item.layoutRes, parent, false)
        ).apply {
            item.onCreateViewHolder(parent = parent, position = viewType)
        }
    }

    final override fun getItemViewType(position: Int): Int = position

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: BindableViewHolder<out ViewDataBinding>, position: Int) {
        holder.bind(position)
    }

    class BindableViewHolder<B : ViewDataBinding>(
        private val item: BindableItem<B>,
        private val binding: B
    ) : ViewHolder(binding.root) {
        fun bind(position: Int) {
            item.bind(binding = binding, position = position)
        }
    }

    private inner class DiffUtilCallback<T : BindableItem<*>> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
            oldItem.areItemsTheSame(newItem)

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
            oldItem.areContentsTheSame(newItem)
    }
}