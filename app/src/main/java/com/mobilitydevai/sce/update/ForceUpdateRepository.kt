/**
 * Created by chrystian.lozada.local (1/11/26)
 */
package com.mobilitydevai.sce.update

import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.mobilitydevai.sce.BuildConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Reads the minimum supported versionCode from Firebase Remote Config
 * (parameter [KEY_MIN_VERSION_CODE]). Any installed version below it must update.
 */
@Singleton
class ForceUpdateRepository @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig,
) {

    companion object {
        const val KEY_MIN_VERSION_CODE = "min_version_code"
        private const val TAG = "ForceUpdate"

        // Debug: always fetch. Release: cache for 1 hour
        private val MIN_FETCH_INTERVAL_SECONDS = if (BuildConfig.DEBUG) 0L else 3600L
    }

    // Must complete before fetching, otherwise the default 12h fetch interval is used
    private val setupTask = Tasks.whenAll(
        remoteConfig.setConfigSettingsAsync(
            remoteConfigSettings { minimumFetchIntervalInSeconds = MIN_FETCH_INTERVAL_SECONDS }
        ),
        remoteConfig.setDefaultsAsync(mapOf(KEY_MIN_VERSION_CODE to 0L))
    )

    /** Last activated value (cached on device). 0 by default, so nothing is blocked. */
    fun cachedMinVersionCode(): Long = remoteConfig.getLong(KEY_MIN_VERSION_CODE)

    /**
     * Fetches the latest config (respecting the minimum fetch interval) and returns
     * the min version code. If fetching fails (e.g. offline) the cached value is used.
     */
    suspend fun fetchMinVersionCode(): Long {
        try {
            setupTask.await()
            val activated = remoteConfig.fetchAndActivate().await()
            Log.i(TAG, "fetchAndActivate ok, new values activated = $activated")
            // Diagnostic logs: where the value comes from (REMOTE = Firebase console, DEFAULT = in-app)
            val value = remoteConfig.getValue(KEY_MIN_VERSION_CODE)
            val source = when (value.source) {
                FirebaseRemoteConfig.VALUE_SOURCE_REMOTE -> "REMOTE"
                FirebaseRemoteConfig.VALUE_SOURCE_DEFAULT -> "DEFAULT (in-app)"
                else -> "STATIC (not found)"
            }
            Log.i(TAG, "raw value = '${value.asString()}', source = $source")
            Log.i(TAG, "remote keys = ${remoteConfig.getKeysByPrefix("")}")
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.w(TAG, "Remote Config fetch failed, using cached value", e)
        }
        val minVersionCode = cachedMinVersionCode()
        Log.i(TAG, "min_version_code = $minVersionCode")
        return minVersionCode
    }
}
