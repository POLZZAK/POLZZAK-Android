package com.polzzak_android.presentation.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.polzzak_android.databinding.ItemStampNonBinding
import com.polzzak_android.databinding.ItemStampYesBinding
import com.polzzak_android.presentation.main.intercation.MainProgressInteraction
import com.polzzak_android.presentation.main.model.StampBoard

class MainStampAdapter(private val dummy: List<StampBoard>, private val interaction: MainProgressInteraction) :
    ListAdapter<StampBoard, RecyclerView.ViewHolder>(DiffCallback) {

    private var stampList = dummy
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
                YesViewHolder(view, interaction)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val curItem = stampList[position]

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
        return stampList.size
    }

    override fun getItemViewType(position: Int): Int {
        return stampList[position].type
    }

    fun setStampList(newList: List<StampBoard>) {
        stampList = newList
        notifyDataSetChanged()
    }

    inner class NonViewHolder(binding: ItemStampNonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val userHeaderTxt = binding.userNickNameHeader
        private val userContentTxt = binding.userNickNameContent

        fun bind(item: StampBoard) {
            userHeaderTxt.text = item.partner.nickname
            userContentTxt.text = item.partner.nickname
        }
    }

    inner class YesViewHolder(binding: ItemStampYesBinding, interaction: MainProgressInteraction) :
        RecyclerView.ViewHolder(binding.root) {
        private val userHeaderTxt = binding.userNickName
        private val stampPager = binding.stampPager
        private val curInd = binding.curPage
        private val totalInd = binding.totalPage

        init {
            // transform
            val currentVisibleItemPx = 50

            stampPager.addItemDecoration(object: RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    outRect.right = currentVisibleItemPx
                    outRect.left = currentVisibleItemPx
                }
            })

            val nextVisibleItemPx = 20
            val pageTranslationX = nextVisibleItemPx + currentVisibleItemPx

            stampPager.offscreenPageLimit = 1

            stampPager.setPageTransformer { page, position ->
                page.translationX = -pageTranslationX * (position)
            }
        }

        fun bind(item: StampBoard) {
            userHeaderTxt.text = item.partner.nickname

            interaction.setViewPager(stampPager, curInd, totalInd, item.stampBoardSummaries)
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
