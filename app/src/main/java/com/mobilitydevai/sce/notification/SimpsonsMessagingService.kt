/**
 * Created by chrystian.lozada.local (24/11/25)
 */
package com.mobilitydevai.sce.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mobilitydevai.sce.MainActivity
import com.mobilitydevai.sce.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SimpsonsMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    companion object {
        private const val TAG = "SimpsonsMessagingService"
        private const val NOTIFICATION_ID = 0
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "=== SimpsonsMessagingService CREATED ===")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "=== SimpsonsMessagingService DESTROYED ===")
    }

    /**
     * Called when a new FCM token is generated
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "=== NEW FCM TOKEN ===")
        Log.d(TAG, "Token: $token")

        // Save token to preferences or send to your server
        notificationHelper.saveToken(token)
        notificationHelper.sendTokenToServer(token)
    }

    /**
     * Called when a message is received
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d(TAG, "========================================")
        Log.d(TAG, "=== MESSAGE RECEIVED ===")
        Log.d(TAG, "========================================")
        Log.d(TAG, "From: ${remoteMessage.from}")
        Log.d(TAG, "Message ID: ${remoteMessage.messageId}")
        Log.d(TAG, "Message Type: ${remoteMessage.messageType}")
        Log.d(TAG, "Sent Time: ${remoteMessage.sentTime}")
        Log.d(TAG, "TTL: ${remoteMessage.ttl}")

        // Log notification payload
        remoteMessage.notification?.let { notification ->
            Log.d(TAG, "--- Notification Payload ---")
            Log.d(TAG, "Title: ${notification.title}")
            Log.d(TAG, "Body: ${notification.body}")
            Log.d(TAG, "Icon: ${notification.icon}")
            Log.d(TAG, "Sound: ${notification.sound}")
            Log.d(TAG, "Tag: ${notification.tag}")
            Log.d(TAG, "Color: ${notification.color}")
            Log.d(TAG, "Click Action: ${notification.clickAction}")
        } ?: Log.d(TAG, "No notification payload")

        // Log data payload
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "--- Data Payload ---")
            remoteMessage.data.forEach { (key, value) ->
                Log.d(TAG, "$key: $value")
            }
        } else {
            Log.d(TAG, "No data payload")
        }

        Log.d(TAG, "========================================")

        // Log notification
        notificationHelper.logNotificationReceived(remoteMessage)

        // Check if message contains a data payload
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Handling data message...")
            handleDataMessage(remoteMessage.data)
        }

        // Check if message contains a notification payload
        remoteMessage.notification?.let { notification ->
            Log.d(TAG, "Showing notification from notification payload...")
            sendNotification(
                title = notification.title ?: "Simpsons Info",
                message = notification.body ?: "",
                data = remoteMessage.data
            )
        }

        // If no notification payload, but data exists, show custom notification
        if (remoteMessage.notification == null && remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Showing notification from data payload...")
            val title = remoteMessage.data["title"] ?: "Simpsons Info"
            val body = remoteMessage.data["body"] ?: "You have a new notification"
            sendNotification(title, body, remoteMessage.data)
        }

        Log.d(TAG, "=== onMessageReceived COMPLETED ===")
    }

    /**
     * Handle data message
     */
    private fun handleDataMessage(data: Map<String, String>) {
        val type = data["type"]

        when (type) {
            "new_character" -> {
                // Handle new character notification
                Log.d(TAG, "New character notification: ${data["character_name"]}")
            }
            "new_location" -> {
                // Handle new location notification
                Log.d(TAG, "New location notification: ${data["location_name"]}")
            }
            "update" -> {
                // Handle app update notification
                Log.d(TAG, "App update notification")
            }
            else -> {
                Log.d(TAG, "Unknown notification type: $type")
            }
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message
     */
    private fun sendNotification(
        title: String,
        message: String,
        data: Map<String, String> = emptyMap()
    ) {
        Log.d(TAG, "sendNotification() called")
        Log.d(TAG, "Title: $title")
        Log.d(TAG, "Message: $message")
        Log.d(TAG, "Data: $data")

        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            // Add data to intent for deep linking
            data.forEach { (key, value) ->
                putExtra(key, value)
            }
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = NotificationHelper.CHANNEL_ID_DEFAULT
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.simpsons_logo)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel for Android O+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "General Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications about Simpsons characters and locations"
            }
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())

        Log.d(TAG, "Notification displayed successfully!")

        // Log notification shown
        notificationHelper.logNotificationShown(title, message)
    }

    /**
     * Called when the FCM server deletes pending messages
     */
    override fun onDeletedMessages() {
        super.onDeletedMessages()
        Log.d(TAG, "Messages deleted on FCM server")
    }

    /**
     * Called when a message couldn't be sent
     */
    override fun onSendError(msgId: String, exception: Exception) {
        super.onSendError(msgId, exception)
        Log.e(TAG, "Failed to send message: $msgId", exception)
        notificationHelper.logNotificationError(msgId, exception)
    }
}
