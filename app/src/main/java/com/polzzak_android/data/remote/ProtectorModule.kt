package com.polzzak_android.data.remote

import com.polzzak_android.presentation.main.protector.completed.ProtectorCompletedFragment
import com.polzzak_android.presentation.main.protector.progress.ProtectorProgressFragment
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