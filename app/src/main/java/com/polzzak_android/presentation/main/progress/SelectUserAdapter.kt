package com.polzzak_android.presentation.main.progress

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.polzzak_android.databinding.ItemSelectUserFilterBinding

class SelectUserAdapter(
    private val dummy: List<String>,
    private val interaction: SelectUserInteraction
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var stampList = dummy
    //private var stampList: listOf<StampInfo>() = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemSelectUserFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view, interaction)
    }

    override fun getItemCount(): Int {
        return stampList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val curItem = stampList[position]

        (holder as ViewHolder).bind(curItem)
    }

    fun setStampList(newList: List<String>) {
        stampList = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: ItemSelectUserFilterBinding, interaction: SelectUserInteraction) :
        RecyclerView.ViewHolder(binding.root) {
        private val container = binding.userFilterContainer
        private val userName = binding.userFilterName

        init {
            container.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = stampList[position]
                    interaction.onUserClicked(item)
                }
            }
        }

        fun bind(item: String) {
            userName.text = item
        }
    }
}
