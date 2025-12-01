package com.ble1st.connectias_launcher

import android.app.Application
import android.util.Log
import com.ble1st.connectias_launcher.features.safemode.CrashHandler
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class ConnectiasLauncherApp : Application() {

    @Inject
    lateinit var crashHandler: CrashHandler

    override fun onCreate() {
        super.onCreate()
        
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }

        crashHandler.init()
    }

    /** A tree which logs important information for crash reporting. */
    private class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }
            
            // Placeholder for Crash Reporting (e.g., Firebase Crashlytics)
            // if (t != null) Crashlytics.logException(t)
        }
    }
}
