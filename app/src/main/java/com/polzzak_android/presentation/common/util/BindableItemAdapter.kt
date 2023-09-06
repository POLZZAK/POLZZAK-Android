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
    private var rv: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        rv = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        rv = null
    }

    fun updateItem(item: List<BindableItem<*>>, commitCallback: (() -> Unit)? = null) {
        submitList(item, commitCallback)
        notifyDataSetChanged()
    }

    fun addItem(items: List<BindableItem<*>>, commitCallback: (() -> Unit)? = null) {
        val newItems = currentList + items
        submitList(newItems, commitCallback)
    }

    private fun submitList(items: List<BindableItem<*>>, commitCallback: (() -> Unit)? = null) {
        asyncDiffer.submitList(items) {
            rv?.invalidateItemDecorations()
            commitCallback?.invoke()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BindableViewHolder<out ViewDataBinding> {
        val layoutInflater = LayoutInflater.from(parent.context)
        return BindableViewHolder(
            binding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
        )
    }

    final override fun getItemViewType(position: Int): Int = currentList[position].layoutRes

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: BindableViewHolder<out ViewDataBinding>, position: Int) {
        holder.bind(position)
    }

    inner class BindableViewHolder<B : ViewDataBinding>(
        private val binding: B
    ) : ViewHolder(binding.root) {
        fun bind(position: Int) {
            @Suppress("UNCHECKED_CAST")
            (currentList[position] as? BindableItem<B>)?.bind(
                binding = binding,
                position = position
            )
        }
    }

    private inner class DiffUtilCallback<T : BindableItem<*>> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
            oldItem.areItemsTheSame(newItem)

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
            oldItem.areContentsTheSame(newItem)
    }
}