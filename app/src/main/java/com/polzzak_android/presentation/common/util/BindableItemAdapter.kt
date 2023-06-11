package com.polzzak_android.presentation.common.util

import android.view.ViewGroup
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
    RecyclerView.Adapter<BindableItemAdapter.BindableViewHolder>() {
    private val asyncDiffer = AsyncListDiffer(this, DiffUtilCallback())
    val currentList: List<BindableItem<*>> get() = asyncDiffer.currentList

    fun updateItem(item: List<BindableItem<*>>) {
        asyncDiffer.submitList(item)
    }

    fun addItem(items: List<BindableItem<*>>) {
        val newItems = currentList + items
        asyncDiffer.submitList(newItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindableViewHolder {
        val item = currentList[viewType]
        val binding = item.createBinding(viewGroup = parent)
        return BindableViewHolder(item = item, binding = binding).apply {
            item.onCreateViewHolder(parent = parent, position = viewType)
        }
    }

    final override fun getItemViewType(position: Int): Int = position

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: BindableViewHolder, position: Int) {
        holder.bind(position)
    }

    class BindableViewHolder(
        private val item: BindableItem<*>,
        private val binding: ViewDataBinding
    ) : ViewHolder(binding.root) {
        fun bind(position: Int) {
            item.bind(position = position)
        }
    }

    private inner class DiffUtilCallback<T : BindableItem<*>> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
            oldItem.areItemsTheSame(newItem)

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
            oldItem.areContentsTheSame(newItem)
    }
}