/**
 * Created by chrystian.lozada.local (8/10/25)
 */
package com.mobilitydevai.sce

import android.app.Application
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.mobilitydevai.sce.analytics.AnalyticsManager
import com.mobilitydevai.sce.analytics.CrashlyticsManager
import com.mobilitydevai.sce.notification.NotificationHelper
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class SimpsonsInfoApp : Application() {

    @Inject
    lateinit var analyticsManager: AnalyticsManager

    @Inject
    lateinit var crashlyticsManager: CrashlyticsManager

    @Inject
    lateinit var notificationHelper: NotificationHelper

    companion object {
        private const val TAG = "SimpsonsInfoApp"
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Initialize Crashlytics
        crashlyticsManager.setCrashlyticsCollectionEnabled(true)
        crashlyticsManager.log("App started - Version: ${BuildConfig.VERSION_NAME}")

        // Initialize Notification Channels
        notificationHelper.createNotificationChannels()

        // Get FCM token
        getFCMToken()

        // Log app start
        analyticsManager.logCustomEvent("app_start", mapOf(
            "version" to BuildConfig.VERSION_NAME,
            "build_type" to BuildConfig.BUILD_TYPE,
            "flavor" to BuildConfig.FLAVOR
        ))
    }

    private fun getFCMToken() {
        Log.d(TAG, "Requesting FCM token...")
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e(TAG, "========================================")
                Log.e(TAG, "ERROR: Fetching FCM token FAILED!")
                Log.e(TAG, "========================================")
                Log.e(TAG, "Exception: ${task.exception?.message}", task.exception)
                crashlyticsManager.recordException(
                    task.exception ?: Exception("Failed to get FCM token")
                )
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.d(TAG, "========================================")
            Log.d(TAG, "=== FCM TOKEN RETRIEVED SUCCESSFULLY ===")
            Log.d(TAG, "========================================")
            Log.d(TAG, "Token: $token")
            Log.d(TAG, "Token length: ${token.length}")
            Log.d(TAG, "========================================")

            // Save token
            notificationHelper.saveToken(token)

            Log.d(TAG, "FCM Token saved to SharedPreferences")
        })
    }
}