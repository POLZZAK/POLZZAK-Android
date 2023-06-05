package com.polzzak_android.presentation.auth.signup.model

data class MemberTypeUiModel(
    val selectedType: Type? = null,
    val selectedTypeId: Int? = null
) {
    enum class Type {
        PARENT,
        KID
    }

    fun isParent() = (selectedType == Type.PARENT)
    fun isKid() = (selectedType == Type.KID)
}
