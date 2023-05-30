package com.polzzak_android.data.remote

import com.polzzak_android.data.repository.MockStampRepositoryImpl
import com.polzzak_android.data.repository.StampRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideStampRepository(): StampRepository = MockStampRepositoryImpl()
}