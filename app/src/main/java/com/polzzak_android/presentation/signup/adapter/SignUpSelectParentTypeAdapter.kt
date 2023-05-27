package com.polzzak_android.presentation.signup.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.polzzak_android.common.model.MemberType
import com.polzzak_android.databinding.ItemSignupParentTypeBinding

class SignUpSelectParentTypeAdapter :
    RecyclerView.Adapter<SignUpSelectParentTypeAdapter.SignUpParentTypeItemViewHolder>() {

    inner class SignUpParentTypeItemViewHolder(private val binding: ItemSignupParentTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(parentType: MemberType.Parent?) {
            binding.tvTitle.text = parentType?.name ?: "선택해주세요"
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SignUpParentTypeItemViewHolder {
        val binding =
            ItemSignupParentTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SignUpParentTypeItemViewHolder(binding)
    }

    override fun getItemCount() = parentTypes.size

    override fun onBindViewHolder(holder: SignUpParentTypeItemViewHolder, position: Int) {
        holder.bind(parentTypes[position])
    }

    companion object {
        val parentTypes = listOf(
            null,
            MemberType.Parent.Mother(),
            MemberType.Parent.Father(),
            MemberType.Parent.FemaleSister(),
            MemberType.Parent.MaleSister(),
            MemberType.Parent.FemaleBrother(),
            MemberType.Parent.MaleBrother(),
            MemberType.Parent.GrandMother(),
            MemberType.Parent.GrandFather(),
            MemberType.Parent.MaternalAunt(),
            MemberType.Parent.PaternalAunt(),
            MemberType.Parent.Uncle(),
            MemberType.Parent.Etc()
        )

        fun getSelectedType(position: Int) = parentTypes.getOrNull(position)

        fun getTypePosition(parentType: MemberType.Parent?) =
            parentTypes.indexOf(parentType).takeIf { it >= 0 } ?: 0
    }


}