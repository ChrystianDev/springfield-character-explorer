/**
 * Created by chrystian.lozada.local (24/11/25)
 */
package com.mobilitydevai.sce.analytics

//import com.google.firebase.Firebase
import com.google.firebase.crashlytics.FirebaseCrashlytics
//import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CrashlyticsManager @Inject constructor() {

    private val crashlytics: FirebaseCrashlytics by lazy {
        Firebase.crashlytics
    }

    /**
     * Set user identifier for crash reports
     */
    fun setUserId(userId: String) {
        crashlytics.setUserId(userId)
    }

    /**
     * Set custom keys to add context to crash reports
     */
    fun setCustomKey(key: String, value: String) {
        crashlytics.setCustomKey(key, value)
    }

    fun setCustomKey(key: String, value: Boolean) {
        crashlytics.setCustomKey(key, value)
    }

    fun setCustomKey(key: String, value: Int) {
        crashlytics.setCustomKey(key, value)
    }

    fun setCustomKey(key: String, value: Long) {
        crashlytics.setCustomKey(key, value)
    }

    fun setCustomKey(key: String, value: Float) {
        crashlytics.setCustomKey(key, value)
    }

    /**
     * Log custom messages to be included in crash reports
     */
    fun log(message: String) {
        crashlytics.log(message)
    }

    /**
     * Record non-fatal exceptions
     */
    fun recordException(throwable: Throwable) {
        crashlytics.recordException(throwable)
    }

    /**
     * Log authentication-related errors
     */
    fun logAuthenticationError(errorMessage: String, exception: Throwable? = null) {
        setCustomKey("error_type", "authentication")
        log("Authentication Error: $errorMessage")
        exception?.let { recordException(it) }
    }

    /**
     * Log network-related errors
     */
    fun logNetworkError(errorMessage: String, exception: Throwable? = null) {
        setCustomKey("error_type", "network")
        log("Network Error: $errorMessage")
        exception?.let { recordException(it) }
    }

    /**
     * Log database-related errors
     */
    fun logDatabaseError(errorMessage: String, exception: Throwable? = null) {
        setCustomKey("error_type", "database")
        log("Database Error: $errorMessage")
        exception?.let { recordException(it) }
    }

    /**
     * Log voting-related errors
     */
    fun logVotingError(imageId: String, errorMessage: String, exception: Throwable? = null) {
        setCustomKey("error_type", "voting")
        setCustomKey("image_id", imageId)
        log("Voting Error for $imageId: $errorMessage")
        exception?.let { recordException(it) }
    }

    /**
     * Enable or disable crash collection
     */
    fun setCrashlyticsCollectionEnabled(enabled: Boolean) {
        crashlytics.setCrashlyticsCollectionEnabled(enabled)
    }

    /**
     * Send any unsent crash reports immediately
     */
    fun sendUnsentReports() {
        crashlytics.sendUnsentReports()
    }

    /**
     * Check if there are any unsent crash reports
     */
    fun checkForUnsentReports() {
        crashlytics.checkForUnsentReports()
    }

    /**
     * Delete any unsent crash reports
     */
    fun deleteUnsentReports() {
        crashlytics.deleteUnsentReports()
    }

    /**
     * Record a breadcrumb for debugging
     */
    fun recordBreadcrumb(screen: String, action: String) {
        log("Breadcrumb: Screen=$screen, Action=$action")
    }

    /**
     * Set user session information
     */
    fun setUserSession(
        userId: String?,
        userName: String?,
        userEmail: String?
    ) {
        userId?.let { setUserId(it) }
        userName?.let { setCustomKey("user_name", it) }
        userEmail?.let { setCustomKey("user_email", it) }
    }
}
