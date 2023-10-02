package com.polzzak_android.data.di

import com.polzzak_android.data.remote.service.NotificationService
import com.polzzak_android.data.repository.NotificationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {
    @Singleton
    @Provides
    fun provideNotificationService(@Named("Polzzak") retrofit: Retrofit): NotificationService =
        retrofit.create(NotificationService::class.java)

    @Singleton
    @Provides
    fun provideNotificationRepository(notificationService: NotificationService): NotificationRepository =
        NotificationRepository(notificationService = notificationService)
}