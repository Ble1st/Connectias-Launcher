package com.ble1st.connectias_launcher.di

import android.content.Context
import android.content.pm.LauncherApps
import android.os.UserManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SystemServiceModule {

    @Provides
    @Singleton
    fun provideLauncherApps(@ApplicationContext context: Context): LauncherApps {
        return context.getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps
    }

    @Provides
    @Singleton
    fun provideUserManager(@ApplicationContext context: Context): UserManager {
        return context.getSystemService(Context.USER_SERVICE) as UserManager
    }
}
