package com.ble1st.connectias_launcher.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shortcuts")
data class ShortcutEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val packageName: String,
    val className: String,
    val label: String,
    val iconPath: String? = null, // Path to cached icon if needed, otherwise load dynamically
    val gridX: Int,
    val gridY: Int,
    val screenId: Int = 0,
    val userProfileId: Long = 0 // For multi-user/work profile support later
)
