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

    // ========== TESTING METHODS - For Debug/Testing Only ==========

    /**
     * Force a crash for testing Crashlytics
     * WARNING: This will crash the app! Only use for testing.
     */
    fun forceCrash() {
        setCustomKey("test_type", "forced_crash")
        log("Testing Crashlytics - Force crash initiated")
        throw RuntimeException("Test Crash - Crashlytics verification")
    }

    /**
     * Test non-fatal exception logging
     */
    fun testNonFatalException() {
        setCustomKey("test_type", "non_fatal_exception")
        log("Testing Crashlytics - Non-fatal exception")
        recordException(Exception("Test non-fatal exception for Crashlytics"))
    }

    /**
     * Test custom error logging with context
     */
    fun testCustomError() {
        setCustomKey("test_type", "custom_error")
        setCustomKey("test_scenario", "simulated_network_failure")
        log("Testing Crashlytics - Custom error with context")
        logNetworkError(
            "Simulated network timeout for testing",
            Exception("Connection timeout after 30 seconds")
        )
    }

    /**
     * Test breadcrumb trail
     */
    fun testBreadcrumbs() {
        recordBreadcrumb("TestScreen", "test_started")
        log("Step 1: Initialized test")
        recordBreadcrumb("TestScreen", "test_processing")
        log("Step 2: Processing test data")
        recordBreadcrumb("TestScreen", "test_completed")
        log("Step 3: Test completed successfully")
        testNonFatalException()
    }
}
