/**
 * Created by chrystian.lozada.local (24/11/25)
 */
package com.mobilitydevai.sce.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.RemoteMessage
import com.mobilitydevai.sce.R
import com.mobilitydevai.sce.analytics.AnalyticsManager
import com.mobilitydevai.sce.analytics.CrashlyticsManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val analyticsManager: AnalyticsManager,
    private val crashlyticsManager: CrashlyticsManager
) {

    companion object {
        const val CHANNEL_ID_DEFAULT = "simpsons_notifications"
        const val CHANNEL_ID_UPDATES = "app_updates"
        const val CHANNEL_ID_CHARACTERS = "character_updates"
        const val CHANNEL_ID_LOCATIONS = "location_updates"

        private const val PREFS_NAME = "notification_prefs"
        private const val KEY_FCM_TOKEN = "fcm_token"
        private const val TAG = "NotificationHelper"
    }

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    private val notificationManager: NotificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    /**
     * Initialize notification channels
     */
    fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channels = listOf(
                NotificationChannel(
                    CHANNEL_ID_DEFAULT,
                    "General Notifications",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "General app notifications"
                    enableLights(true)
                    enableVibration(true)
                },
                NotificationChannel(
                    CHANNEL_ID_UPDATES,
                    "App Updates",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Notifications about app updates"
                },
                NotificationChannel(
                    CHANNEL_ID_CHARACTERS,
                    "New Characters",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Notifications when new characters are added"
                },
                NotificationChannel(
                    CHANNEL_ID_LOCATIONS,
                    "New Locations",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Notifications when new locations are added"
                }
            )

            channels.forEach { channel ->
                notificationManager.createNotificationChannel(channel)
            }

            Log.d(TAG, "Notification channels created")
            crashlyticsManager.log("Notification channels created successfully")
        }
    }

    /**
     * Save FCM token to SharedPreferences
     */
    fun saveToken(token: String) {
        prefs.edit { putString(KEY_FCM_TOKEN, token) }
        Log.d(TAG, "FCM token saved")
        crashlyticsManager.setCustomKey("fcm_token", token)
        analyticsManager.logCustomEvent("fcm_token_saved", mapOf("token_length" to token.length.toString()))
    }

    /**
     * Get saved FCM token
     */
    fun getToken(): String? {
        return prefs.getString(KEY_FCM_TOKEN, null)
    }

    /**
     * Send token to your server
     * TODO: Implement server API call
     */
    fun sendTokenToServer(token: String) {
        Log.d(TAG, "TODO: Send token to server: $token")
        // TODO: Implement API call to your backend to save the token
        // Example: apiService.saveToken(token, userId)
        analyticsManager.logCustomEvent("fcm_token_sent_to_server", mapOf("token_length" to token.length.toString()))
    }

    /**
     * Log when notification is received
     */
    fun logNotificationReceived(remoteMessage: RemoteMessage) {
        val type = remoteMessage.data["type"] ?: "unknown"
        analyticsManager.logCustomEvent("notification_received", mapOf(
            "from" to (remoteMessage.from ?: "unknown"),
            "type" to type,
            "has_notification" to remoteMessage.notification.toString(),
            "has_data" to remoteMessage.data.isNotEmpty().toString()
        ))
        crashlyticsManager.recordBreadcrumb("Notification", "received:$type")
    }

    /**
     * Log when notification is shown
     */
    fun logNotificationShown(title: String, message: String) {
        analyticsManager.logCustomEvent("notification_shown", mapOf(
            "title" to title,
            "message_length" to message.length.toString()
        ))
        crashlyticsManager.log("Notification shown: $title")
    }

    /**
     * Log notification errors
     */
    fun logNotificationError(msgId: String, exception: Exception) {
        analyticsManager.logError("notification", "Failed to send message: ${exception.message}")
        crashlyticsManager.setCustomKey("error_type", "notification")
        crashlyticsManager.setCustomKey("message_id", msgId)
        crashlyticsManager.recordException(exception)
    }

    /**
     * Show a simple test notification
     */
    fun showTestNotification() {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_DEFAULT)
            .setSmallIcon(R.drawable.simpsons_logo)
            .setContentTitle("Test Notification")
            .setContentText("This is a test notification from Simpsons Info app")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)

        // Log test notification
        analyticsManager.logCustomEvent("test_notification_sent")
        crashlyticsManager.log("Test notification sent")
        Log.d(TAG, "Test notification sent")
    }

    /**
     * Check if notifications are enabled
     */
    fun areNotificationsEnabled(): Boolean {
        return notificationManager.areNotificationsEnabled()
    }

    /**
     * Clear all notifications
     */
    fun clearAllNotifications() {
        notificationManager.cancelAll()
        Log.d(TAG, "All notifications cleared")
    }
}
