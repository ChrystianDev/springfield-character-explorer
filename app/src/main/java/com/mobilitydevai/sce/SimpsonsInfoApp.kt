/**
 * Created by chrystian.lozada.local (8/10/25)
 */
package com.mobilitydevai.sce

import android.app.Application
import com.google.firebase.FirebaseApp
import com.mobilitydevai.sce.analytics.AnalyticsManager
import com.mobilitydevai.sce.analytics.CrashlyticsManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class SimpsonsInfoApp : Application() {

    @Inject
    lateinit var analyticsManager: AnalyticsManager

    @Inject
    lateinit var crashlyticsManager: CrashlyticsManager

    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Initialize Crashlytics
        crashlyticsManager.setCrashlyticsCollectionEnabled(true)
        crashlyticsManager.log("App started - Version: ${BuildConfig.VERSION_NAME}")

        // Log app start
        analyticsManager.logCustomEvent("app_start", mapOf(
            "version" to BuildConfig.VERSION_NAME,
            "build_type" to BuildConfig.BUILD_TYPE,
            "flavor" to BuildConfig.FLAVOR
        ))
    }
}