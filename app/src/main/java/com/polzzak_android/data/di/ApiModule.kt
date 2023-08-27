package com.polzzak_android.data.di

import android.content.Context
import com.polzzak_android.BuildConfig
import com.polzzak_android.data.remote.service.AuthService
import com.polzzak_android.data.remote.service.GoogleOAuthService
import com.polzzak_android.data.remote.service.StampBoardService
import com.polzzak_android.data.repository.LastSocialLoginRepository
import com.polzzak_android.data.repository.LoginRepository
import com.polzzak_android.data.repository.SignUpRepository
import com.polzzak_android.data.repository.StampBoardRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    //TODO dev, prod url 구분
    private const val BASE_URL = "https://api.polzzak.co.kr"
    private const val GOOGLE_OAUTH_BASE_URL = "https://www.googleapis.com"

    //TODO 필요 시 timeout 등 옵션 추가
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
        val request = chain.request().newBuilder()
            .header("Content-Type", "application/json;charset=UTF-8")
        chain.proceed(request.build())
    }.addNetworkInterceptor(HttpLoggingInterceptor().apply {
        level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }).build()

    @Singleton
    @Provides
    @Named("Polzzak")
    fun provideRetrofit(okhttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder().client(okhttpClient).baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()

    @Singleton
    @Provides
    @Named("GoogleOAuth")
    fun provideGoogleOAuthRetrofit(): Retrofit = Retrofit.Builder().baseUrl(GOOGLE_OAUTH_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()

    @Singleton
    @Provides
    fun provideAuthService(@Named("Polzzak") retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Singleton
    @Provides
    fun provideGoogleTokenService(@Named("GoogleOAuth") retrofit: Retrofit): GoogleOAuthService =
        retrofit.create(GoogleOAuthService::class.java)

    @Singleton
    @Provides
    fun provideLoginRepository(
        authService: AuthService,
        googleTokenService: GoogleOAuthService
    ): LoginRepository =
        LoginRepository(authService = authService, googleTokenService = googleTokenService)

    @Singleton
    @Provides
    fun provideLastSocialLoginRepository(@ApplicationContext context: Context): LastSocialLoginRepository =
        LastSocialLoginRepository(context)

    @Singleton
    @Provides
    fun provideSignUpRepository(
        authService: AuthService
    ): SignUpRepository = SignUpRepository(authService = authService)

    @Singleton
    @Provides
    fun provideStampBoardRepository(
        stampBoardService: StampBoardService
    ): StampBoardRepository = StampBoardRepository(stampBoardService = stampBoardService)

    @Singleton
    @Provides
    fun provideStampBoardService(@Named("Polzzak") retrofit: Retrofit): StampBoardService =
        retrofit.create(StampBoardService::class.java)
}