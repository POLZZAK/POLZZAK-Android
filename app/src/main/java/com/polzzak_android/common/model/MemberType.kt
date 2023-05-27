package com.polzzak_android.common.model

sealed interface MemberType {
    val name: String

    class Kid(override val name: String = "아이") : MemberType
    sealed interface Parent : MemberType {
        class Mother(override val name: String = "엄마") : Parent
        class Father(override val name: String = "아빠") : Parent
        class FemaleSister(override val name: String = "언니") : Parent
        class MaleSister(override val name: String = "누나") : Parent
        class FemaleBrother(override val name: String = "오빠") : Parent
        class MaleBrother(override val name: String = "형") : Parent
        class GrandMother(override val name: String = "할머니") : Parent
        class GrandFather(override val name: String = "할아버지") : Parent
        class MaternalAunt(override val name: String = "이모") : Parent
        class PaternalAunt(override val name: String = "고모") : Parent
        class Uncle(override val name: String = "삼촌") : Parent
        class Etc(override val name: String = "보호자") : Parent
    }
}