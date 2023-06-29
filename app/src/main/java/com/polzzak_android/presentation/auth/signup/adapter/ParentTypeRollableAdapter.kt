package com.polzzak_android.presentation.auth.signup.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.polzzak_android.databinding.ItemSignupParentTypeBinding
import com.polzzak_android.presentation.auth.model.MemberTypeDetail

class ParentTypeRollableAdapter(
    private val parentTypes: List<MemberTypeDetail.Parent?> = listOf(
        null
    )
) : RecyclerView.Adapter<ParentTypeRollableAdapter.SignUpParentTypeItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SignUpParentTypeItemViewHolder {
        val binding =
            ItemSignupParentTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SignUpParentTypeItemViewHolder(binding)
    }

    override fun getItemCount(): Int = if (parentTypes.isEmpty()) 0 else Int.MAX_VALUE

    override fun onBindViewHolder(holder: SignUpParentTypeItemViewHolder, position: Int) {
        holder.bind(parentTypes[position % parentTypes.size])
    }

    fun getSelectedType(position: Int) = parentTypes.getOrNull(position % parentTypes.size)

    private fun getTypeItemPosition(parentTypeId: Int?): Int =
        parentTypes.indexOfFirst { it?.id == parentTypeId }

    fun getTypeStartPosition(parentTypeId: Int?): Int {
        val itemPosition = getTypeItemPosition(parentTypeId)
        return (itemCount / 2).let { it - it % parentTypes.size + itemPosition }
    }

    inner class SignUpParentTypeItemViewHolder(private val binding: ItemSignupParentTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(parentType: MemberTypeDetail.Parent?) {
            binding.root.tag = if (parentType == null) ITEM_EMPTY_TAG else ITEM_NORMAL_TAG
            binding.tvTitle.text = parentType?.label ?: "선택해주세요"
        }
    }

    companion object {
        const val ITEM_EMPTY_TAG = "item_empty_tag"
        const val ITEM_NORMAL_TAG = "item_normal_tag"
    }
}