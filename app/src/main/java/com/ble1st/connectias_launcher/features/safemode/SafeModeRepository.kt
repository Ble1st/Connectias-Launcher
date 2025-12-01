package com.ble1st.connectias_launcher.features.safemode

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

interface SafeModeRepository {
    fun isSystemSafeMode(): Boolean
    fun isCrashLoopDetected(): Boolean
    fun recordCrash()
    fun resetCrashCount()
}

@Singleton
class SafeModeRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SafeModeRepository {

    private val prefs: SharedPreferences = context.getSharedPreferences("safe_mode_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_CRASH_COUNT = "crash_count"
        private const val KEY_LAST_CRASH_TIMESTAMP = "last_crash_timestamp"
        private const val CRASH_THRESHOLD = 3
        private const val RESET_TIME_MS = 60 * 1000L // 1 Minute
    }

    override fun isSystemSafeMode(): Boolean {
        return context.packageManager.isSafeMode
    }

    override fun isCrashLoopDetected(): Boolean {
        val count = prefs.getInt(KEY_CRASH_COUNT, 0)
        return count >= CRASH_THRESHOLD
    }

    override fun recordCrash() {
        val now = System.currentTimeMillis()
        val lastCrash = prefs.getLong(KEY_LAST_CRASH_TIMESTAMP, 0)
        var count = prefs.getInt(KEY_CRASH_COUNT, 0)

        if (now - lastCrash < RESET_TIME_MS) {
            count++
        } else {
            count = 1
        }

        prefs.edit()
            .putInt(KEY_CRASH_COUNT, count)
            .putLong(KEY_LAST_CRASH_TIMESTAMP, now)
            .commit() // Synchronous commit is essential during a crash handler
    }

    override fun resetCrashCount() {
         prefs.edit()
             .remove(KEY_CRASH_COUNT)
             .remove(KEY_LAST_CRASH_TIMESTAMP)
             .apply()
    }
}
