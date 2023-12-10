package com.polzzak_android.presentation.component.newbottomsheet.makestamp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.polzzak_android.R
import com.polzzak_android.databinding.ItemSelectingStampBinding
import com.polzzak_android.presentation.feature.stamp.model.StampIcon

/**
 * 도장 선택 리스트 어댑터
 */
class StampListAdapter(
    val onStampClick: (stamp: Int) -> Unit
) : ListAdapter<Int, StampListAdapter.StampViewHolder>(StampDiffUtil()) {

    init {
        submitList((1..9).toList())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StampViewHolder {
        val binding = DataBindingUtil.inflate<ItemSelectingStampBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_selecting_stamp,
            parent,
            false
        )

        return StampViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StampViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class StampViewHolder(
        private val binding: ItemSelectingStampBinding
    ) : ViewHolder(binding.root) {
        private var id: Int? = null

        init {
            binding.stamp.setOnClickListener { _ ->
                id?.also { onStampClick(it) }
            }
        }

        fun bind(id: Int) {
            this.id = id

            binding.stamp.setImageResource(StampIcon.values()[id].resId)
        }
    }
}

/**
 * 도장 선택 리스트 DiffUtil
 */
class StampDiffUtil : DiffUtil.ItemCallback<Int>() {
    override fun areItemsTheSame(
        oldItem: Int,
        newItem: Int
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: Int,
        newItem: Int
    ): Boolean {
        return oldItem == newItem
    }
}

