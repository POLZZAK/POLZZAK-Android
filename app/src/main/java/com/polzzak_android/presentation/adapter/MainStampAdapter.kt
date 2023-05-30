package com.polzzak_android.presentation.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.polzzak_android.databinding.ItemStampNonBinding
import com.polzzak_android.databinding.ItemStampYesBinding
import com.polzzak_android.presentation.main.intercation.MainProgressInteraction
import com.polzzak_android.presentation.main.model.StampBoard

enum class ViewHolderType {
    NON,
    YES
}

class MainStampAdapter(private val interaction: MainProgressInteraction) :
    ListAdapter<StampBoard, MainStampAdapter.ViewHolder>(DiffCallback) {

    private var stampList: List<StampBoard> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ViewHolderType.NON.ordinal -> {
                val itemHomeBannerBinding = ItemStampNonBinding.inflate(inflater, parent, false)
                ViewHolder(itemHomeBannerBinding, interaction)
            }
            ViewHolderType.YES.ordinal -> {
                val fragmentOneDayPopupBinding = ItemStampYesBinding.inflate(inflater, parent, false)
                ViewHolder(fragmentOneDayPopupBinding, interaction)
            }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val curItem = stampList[position]
        holder.bind(curItem)
    }

    override fun getItemCount(): Int {
        return stampList.size
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item.type) {
            1 -> ViewHolderType.NON.ordinal
            else -> ViewHolderType.YES.ordinal
        }
    }

    fun setStampList(newList: List<StampBoard>) {
        stampList = newList
        submitList(newList)
    }

    inner class ViewHolder(
        private val binding: ViewBinding,
        private val interaction: MainProgressInteraction?
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            when (binding) {
                is ItemStampYesBinding -> {
                    // transform : viewPager item 간격 설정
                    val currentVisibleItemPx = 50

                    binding.stampPager.addItemDecoration(object : RecyclerView.ItemDecoration() {
                        override fun getItemOffsets(
                            outRect: Rect,
                            view: View,
                            parent: RecyclerView,
                            state: RecyclerView.State
                        ) {
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
            }
        }

        fun bind(item: StampBoard) {
            when (binding) {
                is ItemStampNonBinding -> {

                    binding.apply {
                        userNickNameHeader.text = item.partner.nickname
                        userNickNameContent.text = item.partner.nickname
                    }
                }

                is ItemStampYesBinding -> {
                    binding.userNickName.text = item.partner.nickname

                    interaction?.setViewPager(
                        binding.stampPager,
                        binding.curPage,
                        binding.totalPage,
                        item.stampBoardSummaries
                    )
                }
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<StampBoard>() {
            override fun areItemsTheSame(oldItem: StampBoard, newItem: StampBoard): Boolean {
                return oldItem.partner.memberId == newItem.partner.memberId
            }

            override fun areContentsTheSame(oldItem: StampBoard, newItem: StampBoard): Boolean {
                return oldItem == newItem
            }
        }
    }


}
