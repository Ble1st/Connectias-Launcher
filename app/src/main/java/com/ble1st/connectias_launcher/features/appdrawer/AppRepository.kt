package com.ble1st.connectias_launcher.features.appdrawer

import android.content.Context
import android.content.pm.LauncherApps
import android.os.Process
import android.os.UserManager
import com.ble1st.connectias_launcher.domain.model.AppInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val launcherApps: LauncherApps,
    private val userManager: UserManager
) {

    suspend fun getAllApps(): List<AppInfo> = withContext(Dispatchers.IO) {
        val appList = mutableListOf<AppInfo>()
        val profiles = userManager.userProfiles

        for (user in profiles) {
            val apps = launcherApps.getActivityList(null, user)
            for (info in apps) {
                appList.add(
                    AppInfo(
                        label = info.label.toString(),
                        packageName = info.applicationInfo.packageName,
                        icon = info.getIcon(0), // 0 density = use system default
                        user = user
                    )
                )
            }
        }
        // Sort alphabetically by label
        appList.sortedBy { it.label.lowercase() }
    }
}
