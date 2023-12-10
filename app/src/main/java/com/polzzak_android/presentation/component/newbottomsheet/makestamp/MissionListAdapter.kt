package com.polzzak_android.presentation.component.newbottomsheet.makestamp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.polzzak_android.R
import com.polzzak_android.databinding.ItemMissionBinding
import com.polzzak_android.databinding.ItemMissionRequestBinding
import com.polzzak_android.presentation.feature.stamp.model.MissionData
import com.polzzak_android.presentation.feature.stamp.model.MissionModel
import com.polzzak_android.presentation.feature.stamp.model.MissionRequestModel
import java.time.format.DateTimeFormatter
import kotlin.properties.Delegates

/**
 * 미션 선택 리스트 어댑터.
 * 들어온 데이터 타입에 따라 알맞은 ViewHolder를 표시합니다.
 */
class MissionListAdapter(
    private val onClick: (id: Int) -> Unit,
    private val onRejectClick: (requestId: Int) -> Unit
) : ListAdapter<MissionData, MissionViewHolder<*>>(MissionDiffUtil()) {

    companion object {
        private const val TYPE_SELECT = 1
        private const val TYPE_REQUEST = 2
    }

    // selected 처리할 뷰홀더에 notify 보내는 observable
    private var selectedPos: Int? by Delegates.observable(null) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            oldValue?.also { notifyItemChanged(it) }
            newValue?.also { notifyItemChanged(it) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MissionViewHolder<*> {
        // 뷰 타입에 따라 뷰홀더 리턴
        return when (viewType) {
            TYPE_SELECT -> {
                DataBindingUtil.inflate<ItemMissionBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_mission,
                    parent,
                    false
                ).let {
                    MissionSelectViewHolder(it)
                }
            }
            TYPE_REQUEST -> {
                DataBindingUtil.inflate<ItemMissionRequestBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_mission_request,
                    parent,
                    false
                ).let {
                    MissionRequestViewHolder(it)
                }
            }
            else -> throw IllegalArgumentException("잘못된 타입")
        }
    }

    override fun onBindViewHolder(holder: MissionViewHolder<*>, position: Int) {
        when (holder) {
            is MissionSelectViewHolder -> holder.bind(getItem(position) as MissionModel)
            is MissionRequestViewHolder -> holder.bind(getItem(position) as MissionRequestModel)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is MissionModel -> TYPE_SELECT
            is MissionRequestModel -> TYPE_REQUEST
            else -> throw IllegalArgumentException("잘못된 타입")
        }
    }

    /**
     * 미션 직접 선택 뷰홀더
     */
    inner class MissionSelectViewHolder(
        private val binding: ItemMissionBinding
    ) : MissionViewHolder<MissionModel>(binding.root) {

        init {
            binding.root.setOnClickListener {
                selectedPos = adapterPosition
                id?.also { onClick(it) }
            }
        }

        override fun bind(data: MissionModel) {
            id = data.id

            binding.apply {
                this.data = data.content

                root.isSelected = (adapterPosition == selectedPos)
                itemExMissionCheck.visibility  = if (root.isSelected) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }

        }
    }

    /**
     * 미션 요청 선택 뷰홀더
     */
    inner class MissionRequestViewHolder(
        private val binding: ItemMissionRequestBinding
    ) : MissionViewHolder<MissionRequestModel>(binding.root) {
        private val formatter = DateTimeFormatter.ofPattern("MM.dd")

        init {
            binding.apply {
                root.setOnClickListener {
                    selectedPos = adapterPosition
                    id?.also { onClick(it) }
                }

                tvReject.setOnClickListener {
                    id?.also { onRejectClick(it) }
                }
            }
        }

        override fun bind(data: MissionRequestModel) {
            id = data.id

            binding.apply {
                root.isSelected = (adapterPosition == selectedPos)

                tvContent.text = data.missionContent
                tvDate.text = data.createdDate.format(formatter)
            }
        }
    }

}

/**
 * 두 뷰홀더를 통일시키기 위한 추상 뷰홀더 클래스
 */
abstract class MissionViewHolder<in T : MissionData>(itemView: View) : ViewHolder(itemView) {
    protected var id: Int? = null

    abstract fun bind(data: T)
}

/**
 * DiffUtil
 */
class MissionDiffUtil : DiffUtil.ItemCallback<MissionData>() {
    override fun areItemsTheSame(oldItem: MissionData, newItem: MissionData): Boolean {
        return when {
            (oldItem is MissionModel) and (newItem is MissionModel) -> {
                (oldItem as MissionModel).id == (newItem as MissionModel).id
            }
            (oldItem is MissionRequestModel) and (newItem is MissionRequestModel) -> {
                (oldItem as MissionRequestModel).id == (newItem as MissionRequestModel).id
            }
            else -> throw IllegalArgumentException("잘못된 타입")
        }
    }

    override fun areContentsTheSame(oldItem: MissionData, newItem: MissionData): Boolean {
        return when {
            (oldItem is MissionModel) and (newItem is MissionModel) -> {
                (oldItem as MissionModel).content == (newItem as MissionModel).content
            }
            (oldItem is MissionRequestModel) and (newItem is MissionRequestModel) -> {
                (oldItem as MissionRequestModel).missionContent == (newItem as MissionRequestModel).missionContent
            }
            else -> throw IllegalArgumentException("잘못된 타입")
        }
    }
}