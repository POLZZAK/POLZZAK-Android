package com.polzzak_android.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.polzzak_android.databinding.ItemStampCountBinding
import com.polzzak_android.presentation.makingStamp.StampCountInteraction
import com.polzzak_android.presentation.makingStamp.StampCountSelectedHelper

class MakeStampCountAdapter(
    private val recyclerView: RecyclerView,
    private val interaction: StampCountInteraction,
    private val stampCountSelectHelper: StampCountSelectedHelper
) :
    ListAdapter<Int, MakeStampCountAdapter.ViewHolder>(DiffCallback) {

    private var countList: List<Int>? = listOf(10, 12, 16, 20, 25, 30, 36, 40, 48, 60)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ItemStampCountBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), interaction
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemStampCountBinding,
        private val interaction: StampCountInteraction
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(value: Int) {
            stampCountSelectHelper.initSelectedCount(view = binding.itemStampCount, value = value)

            binding.data = value
            binding.root.setOnClickListener {
                // 다른 아이템 초기화
                for (i in 0 until itemCount) {
                    if (adapterPosition != i) {
                        val otherViewHolder = recyclerView.findViewHolderForAdapterPosition(i) as? ViewHolder
                        otherViewHolder?.let { viewHolder ->
                            stampCountSelectHelper.resetSelectedCount(view = viewHolder.binding.itemStampCount)
                        }
                    }
                }

                // 아이템 선택
                interaction.onStampCountClicked(view = binding.itemStampCount, value = value)
            }
        }
    }

    override fun submitList(list: List<Int>?) {
        super.submitList(list)
        countList = list
        notifyDataSetChanged()
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Int>() {
            override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
                return oldItem == newItem
            }
        }
    }
}