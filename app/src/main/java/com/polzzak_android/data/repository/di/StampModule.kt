package com.polzzak_android.data.repository.di

import com.polzzak_android.data.repository.MockStampRepositoryImpl
import com.polzzak_android.data.repository.StampRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class StampModule {
    @Binds
    abstract fun bindStampRepository(mockStampRepositoryImpl: MockStampRepositoryImpl): StampRepository
}