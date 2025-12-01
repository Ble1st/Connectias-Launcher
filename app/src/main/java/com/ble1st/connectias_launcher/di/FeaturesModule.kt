package com.ble1st.connectias_launcher.di

import com.ble1st.connectias_launcher.features.safemode.SafeModeRepository
import com.ble1st.connectias_launcher.features.safemode.SafeModeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FeaturesModule {

    @Binds
    abstract fun bindSafeModeRepository(
        safeModeRepositoryImpl: SafeModeRepositoryImpl
    ): SafeModeRepository
}
