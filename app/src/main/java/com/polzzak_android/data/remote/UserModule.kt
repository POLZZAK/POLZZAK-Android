package com.polzzak_android.data.remote

import com.polzzak_android.data.remote.service.UserService
import com.polzzak_android.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {
    @Singleton
    @Provides
    fun provideUserService(@Named("Polzzak") retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)

    @Singleton
    @Provides
    fun provideUserRepository(userService: UserService): UserRepository =
        UserRepository(userService = userService)
}