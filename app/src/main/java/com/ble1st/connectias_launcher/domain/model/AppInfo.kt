package com.ble1st.connectias_launcher.domain.model

import android.graphics.drawable.Drawable
import android.os.UserHandle

data class AppInfo(
    val label: String,
    val packageName: String,
    val icon: Drawable,
    val user: UserHandle
)
