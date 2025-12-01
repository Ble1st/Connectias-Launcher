package com.ble1st.connectias_launcher.features.safemode

import timber.log.Timber
import javax.inject.Inject
import kotlin.system.exitProcess

class CrashHandler @Inject constructor(
    private val safeModeRepository: SafeModeRepository
) : Thread.UncaughtExceptionHandler {

    private var defaultHandler: Thread.UncaughtExceptionHandler? = null

    fun init() {
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        Timber.e(e, "Uncaught exception detected!")

        try {
            // Record the crash synchronously
            safeModeRepository.recordCrash()
        } catch (inner: Exception) {
            // If writing prefs fails, we can't do much, but try to log it
            Timber.e(inner, "Failed to record crash.")
        }

        // Delegate to the default handler (which usually crashes the app visibly)
        defaultHandler?.uncaughtException(t, e) ?: run {
            exitProcess(1)
        }
    }
}
