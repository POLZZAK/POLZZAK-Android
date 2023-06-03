package com.polzzak_android.presentation.onboarding.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.polzzak_android.databinding.ItemOnBoardingPageBinding
import com.polzzak_android.presentation.onboarding.model.OnBoardingPageModel

class OnBoardingPagerAdapter(private val dataList: List<OnBoardingPageModel>) :
    RecyclerView.Adapter<OnBoardingPagerAdapter.OnBoardingPagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingPagerViewHolder {
        val binding =
            ItemOnBoardingPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OnBoardingPagerViewHolder(binding = binding)
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: OnBoardingPagerViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class OnBoardingPagerViewHolder(private val binding: ItemOnBoardingPageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            with(binding) {
                val data = dataList.getOrNull(position) ?: return
                tvTitle.text = data.title
                tvContent.text = data.content
                cpvProgress.isVisible = (position < dataList.lastIndex)
                cpvProgress.checkedCount = position + 1
                cpvProgress.maxCount = dataList.size
                tvBtnStart.isVisible = (position == dataList.lastIndex)
            }
        }
    }
}