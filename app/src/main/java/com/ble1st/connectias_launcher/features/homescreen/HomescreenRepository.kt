package com.ble1st.connectias_launcher.features.homescreen

import com.ble1st.connectias_launcher.data.local.dao.ShortcutDao
import com.ble1st.connectias_launcher.data.local.entity.ShortcutEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomescreenRepository @Inject constructor(
    private val shortcutDao: ShortcutDao
) {
    fun getShortcutsForScreen(screenId: Int): Flow<List<ShortcutEntity>> {
        return shortcutDao.getShortcutsForScreen(screenId)
    }

    suspend fun addShortcut(shortcut: ShortcutEntity) {
        shortcutDao.insertShortcut(shortcut)
    }

    suspend fun removeShortcut(shortcutId: Long) {
        shortcutDao.deleteShortcutById(shortcutId)
    }
    
    suspend fun updateShortcutPosition(shortcut: ShortcutEntity, newX: Int, newY: Int) {
        val updated = shortcut.copy(gridX = newX, gridY = newY)
        shortcutDao.updateShortcut(updated)
    }
}
