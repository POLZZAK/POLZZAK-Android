package com.polzzak_android.data.di

import com.polzzak_android.data.remote.service.MemberTypeService
import com.polzzak_android.data.repository.MemberTypeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MemberTypeModule {
    @Singleton
    @Provides
    fun provideMemberTypeService(@Named("Polzzak") retrofit: Retrofit): MemberTypeService =
        retrofit.create(MemberTypeService::class.java)

    @Singleton
    @Provides
    fun provideMemberTypeRepository(memberTypeService: MemberTypeService): MemberTypeRepository =
        MemberTypeRepository(memberTypeService = memberTypeService)
}