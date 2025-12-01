package com.ble1st.connectias_launcher.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ble1st.connectias_launcher.data.local.dao.ShortcutDao
import com.ble1st.connectias_launcher.data.local.entity.ShortcutEntity

@Database(entities = [ShortcutEntity::class], version = 1, exportSchema = false)
abstract class LauncherDatabase : RoomDatabase() {
    abstract fun shortcutDao(): ShortcutDao
}
