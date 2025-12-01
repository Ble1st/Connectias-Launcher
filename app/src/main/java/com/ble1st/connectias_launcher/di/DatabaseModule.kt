package com.ble1st.connectias_launcher.di

import android.content.Context
import androidx.room.Room
import com.ble1st.connectias_launcher.data.local.LauncherDatabase
import com.ble1st.connectias_launcher.data.local.dao.ShortcutDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideLauncherDatabase(
        @ApplicationContext context: Context
    ): LauncherDatabase {
        return Room.databaseBuilder(
            context,
            LauncherDatabase::class.java,
            "connectias_launcher.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideShortcutDao(database: LauncherDatabase): ShortcutDao {
        return database.shortcutDao()
    }
}
