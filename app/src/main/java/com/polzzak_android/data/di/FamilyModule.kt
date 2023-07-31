package com.polzzak_android.data.di

import com.polzzak_android.data.remote.service.FamilyService
import com.polzzak_android.data.repository.FamilyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FamilyModule {

    @Singleton
    @Provides
    fun provideFamilyService(@Named("Polzzak") retrofit: Retrofit): FamilyService =
        retrofit.create(FamilyService::class.java)

    @Singleton
    @Provides
    fun provideFamilyRepository(familyService: FamilyService): FamilyRepository =
        FamilyRepository(familyService = familyService)
}