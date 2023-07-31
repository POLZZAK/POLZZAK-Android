package com.polzzak_android.presentation.common.di

import com.polzzak_android.presentation.feature.stamp.main.completed.ProtectorCompletedFragment
import com.polzzak_android.presentation.feature.stamp.main.progress.ProtectorProgressFragment
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
object ProtectorModule {
    @Provides
    @FragmentScoped
    fun provideProtectorProgressFragment(): ProtectorProgressFragment {
        return ProtectorProgressFragment()
    }

    @Provides
    @FragmentScoped
    fun provideProtectorCompletedFragment(): ProtectorCompletedFragment {
        return ProtectorCompletedFragment()
    }
}