package com.polzzak_android.common.model

import com.polzzak_android.data.remote.model.RemoteMemberType

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

    fun toRemoteMemberType() = when (this) {
        is Kid -> RemoteMemberType.KID
        is Parent.Mother -> RemoteMemberType.MOTHER
        is Parent.Father -> RemoteMemberType.FATHER
        is Parent.FemaleSister -> RemoteMemberType.FEMALE_SISTER
        is Parent.MaleSister -> RemoteMemberType.MALE_SISTER
        is Parent.FemaleBrother -> RemoteMemberType.FEMALE_BROTHER
        is Parent.MaleBrother -> RemoteMemberType.MALE_BROTHER
        is Parent.GrandMother -> RemoteMemberType.GRANDMOTHER
        is Parent.GrandFather -> RemoteMemberType.GRANDFATHER
        is Parent.MaternalAunt -> RemoteMemberType.MATERNAL_AUNT
        is Parent.PaternalAunt -> RemoteMemberType.PATERNAL_AUNT
        is Parent.Uncle -> RemoteMemberType.UNCLE
        is Parent.Etc -> RemoteMemberType.ETC
    }

    fun asMemberType(remoteMemberType: RemoteMemberType) = when (remoteMemberType) {
        RemoteMemberType.KID -> Kid()
        RemoteMemberType.MOTHER -> Parent.Mother()
        RemoteMemberType.FATHER -> Parent.Father()
        RemoteMemberType.FEMALE_SISTER -> Parent.FemaleSister()
        RemoteMemberType.MALE_SISTER -> Parent.MaleSister()
        RemoteMemberType.FEMALE_BROTHER -> Parent.FemaleBrother()
        RemoteMemberType.MALE_BROTHER -> Parent.MaleBrother()
        RemoteMemberType.GRANDMOTHER -> Parent.GrandMother()
        RemoteMemberType.GRANDFATHER -> Parent.GrandFather()
        RemoteMemberType.MATERNAL_AUNT -> Parent.MaternalAunt()
        RemoteMemberType.PATERNAL_AUNT -> Parent.PaternalAunt()
        RemoteMemberType.UNCLE -> Parent.Uncle()
        RemoteMemberType.ETC -> Parent.Etc()
    }
}