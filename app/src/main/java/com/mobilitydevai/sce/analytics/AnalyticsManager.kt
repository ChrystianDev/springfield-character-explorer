/**
 * Created by chrystian.lozada.local (24/11/25)
 */
package com.mobilitydevai.sce.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
//import com.google.firebase.analytics.ktx.analytics
//import com.google.firebase.analytics.ktx.logEvent
//import com.google.firebase.ktx.Firebase
import com.google.firebase.analytics.logEvent
import com.google.firebase.Firebase    import com.google.firebase.analytics.analytics

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsManager @Inject constructor() {

    private val analytics: FirebaseAnalytics by lazy {
        Firebase.analytics
    }

    /**
     * Log screen view events
     */
    fun logScreenView(screenName: String, screenClass: String? = null) {
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            screenClass?.let {
                param(FirebaseAnalytics.Param.SCREEN_CLASS, it)
            }
        }
    }

    /**
     * Log authentication events
     */
    fun logLogin(method: String) {
        analytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
            param(FirebaseAnalytics.Param.METHOD, method)
        }
    }

    fun logSignUp(method: String) {
        analytics.logEvent(FirebaseAnalytics.Event.SIGN_UP) {
            param(FirebaseAnalytics.Param.METHOD, method)
        }
    }

    /**
     * Log user interactions
     */
    fun logSelectContent(contentType: String, itemId: String) {
        analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
            param(FirebaseAnalytics.Param.CONTENT_TYPE, contentType)
            param(FirebaseAnalytics.Param.ITEM_ID, itemId)
        }
    }

    /**
     * Log like/unlike events
     */
    fun logVote(imageId: String, voteType: String) {
        analytics.logEvent("image_vote") {
            param("image_id", imageId)
            param("vote_type", voteType)
        }
    }

    /**
     * Log character selection
     */
    fun logCharacterView(characterName: String, characterId: String) {
        analytics.logEvent("character_view") {
            param("character_name", characterName)
            param("character_id", characterId)
        }
    }

    /**
     * Log location view
     */
    fun logLocationView(locationName: String, locationId: String) {
        analytics.logEvent("location_view") {
            param("location_name", locationName)
            param("location_id", locationId)
        }
    }

    /**
     * Log theme change
     */
    fun logThemeChange(isDarkMode: Boolean) {
        analytics.logEvent("theme_change") {
            param("theme", if (isDarkMode) "dark" else "light")
        }
    }

    /**
     * Log audio mute/unmute
     */
    fun logAudioToggle(isMuted: Boolean) {
        analytics.logEvent("audio_toggle") {
            param("is_muted", if (isMuted) "true" else "false")
        }
    }

    /**
     * Log search queries
     */
    fun logSearch(searchTerm: String) {
        analytics.logEvent(FirebaseAnalytics.Event.SEARCH) {
            param(FirebaseAnalytics.Param.SEARCH_TERM, searchTerm)
        }
    }

    /**
     * Log page view
     */
    fun logPageView(page: Int, pageType: String) {
        analytics.logEvent("page_view") {
            param("page_number", page.toString())
            param("page_type", pageType)
        }
    }

    /**
     * Set user properties
     */
    fun setUserId(userId: String) {
        analytics.setUserId(userId)
    }

    fun setUserProperty(propertyName: String, propertyValue: String) {
        analytics.setUserProperty(propertyName, propertyValue)
    }

    /**
     * Log custom events with custom parameters
     */
    fun logCustomEvent(eventName: String, params: Map<String, String> = emptyMap()) {
        analytics.logEvent(eventName) {
            params.forEach { (key, value) ->
                param(key, value)
            }
        }
    }

    /**
     * Log errors that are non-fatal
     */
    fun logError(errorType: String, errorMessage: String) {
        analytics.logEvent("app_error") {
            param("error_type", errorType)
            param("error_message", errorMessage)
        }
    }
}
