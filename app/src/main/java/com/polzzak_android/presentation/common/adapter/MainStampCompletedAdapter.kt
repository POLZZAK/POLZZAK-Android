package com.polzzak_android.presentation.common.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.polzzak_android.databinding.ItemStampNonBinding
import com.polzzak_android.databinding.ItemStampYesBinding
import com.polzzak_android.presentation.feature.stamp.intercation.MainCompletedInteraction
import com.polzzak_android.presentation.feature.stamp.intercation.MainProgressInteraction
import com.polzzak_android.presentation.feature.stamp.model.StampBoardModel

class MainStampCompletedAdapter(private val dummy: List<StampBoardModel>, private val interaction: MainCompletedInteraction) :
    ListAdapter<StampBoardModel, RecyclerView.ViewHolder>(DiffCallback) {

    private var completedStampList = dummy
    //private var stampList: listOf<StampInfo>() = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> {
                val view =
                    ItemStampNonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                NonViewHolder(view)
            }
            else -> {
                val view =
                    ItemStampYesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                YesViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val curItem = completedStampList[position]

        when (curItem.type) {
            1 -> {
                (holder as NonViewHolder).bind(curItem)
            }

            else -> {
                (holder as YesViewHolder).bind(curItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return completedStampList.size
    }

    override fun getItemViewType(position: Int): Int {
        return completedStampList[position].type
    }

    fun setCompletedStampList(newList: List<StampBoardModel>) {
        completedStampList = newList
        notifyDataSetChanged()
    }

    inner class NonViewHolder(private val binding: ItemStampNonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StampBoardModel) {
            with(binding) {
                // TODO: 멤버 타입 라벨 표시
                userNickNameHeader.text = item.partner?.nickname
                userNickNameContent.text = item.partner?.nickname
            }
        }
    }

    inner class YesViewHolder(private val binding: ItemStampYesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            // transform
            val currentVisibleItemPx = 50

            binding.stampPager.addItemDecoration(object: RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    outRect.right = currentVisibleItemPx
                    outRect.left = currentVisibleItemPx
                }
            })

            val nextVisibleItemPx = 20
            val pageTranslationX = nextVisibleItemPx + currentVisibleItemPx

            binding.stampPager.offscreenPageLimit = 1

            binding.stampPager.setPageTransformer { page, position ->
                page.translationX = -pageTranslationX * (position)
            }
        }

        fun bind(item: StampBoardModel) {
            with(binding) {
                // TODO: 멤버타입 라벨 표시
                userNickName.text = item.partner?.nickname
                interaction.setViewPager(stampPager, curPage, totalPage, item.stampBoardSummaries)
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<StampBoardModel>() {
            override fun areItemsTheSame(oldItem: StampBoardModel, newItem: StampBoardModel): Boolean {
                return oldItem.partner?.memberId == newItem.partner?.memberId
            }

            override fun areContentsTheSame(oldItem: StampBoardModel, newItem: StampBoardModel): Boolean {
                return oldItem == newItem
            }
        }
    }


}
