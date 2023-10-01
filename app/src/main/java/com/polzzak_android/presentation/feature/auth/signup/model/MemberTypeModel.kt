package com.polzzak_android.presentation.feature.auth.signup.model

data class MemberTypeModel(
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
