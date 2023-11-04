package com.polzzak_android.data.di

import com.polzzak_android.data.remote.service.NotificationService
import com.polzzak_android.data.remote.service.PushMessageService
import com.polzzak_android.data.repository.NotificationRepository
import com.polzzak_android.data.repository.PushMessageRepository
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

    @Singleton
    @Provides
    fun providePushMessageService(@Named("Polzzak") retrofit: Retrofit): PushMessageService =
        retrofit.create(PushMessageService::class.java)

    @Singleton
    @Provides
    fun providePushMessageRepository(pushMessageService: PushMessageService): PushMessageRepository =
        PushMessageRepository(pushMessageService = pushMessageService)
}