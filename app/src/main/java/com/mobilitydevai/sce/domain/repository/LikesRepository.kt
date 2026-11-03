/**
 * Created by chrystian.lozada.local (1/11/26)
 */
package com.mobilitydevai.sce.domain.repository

import kotlinx.coroutines.flow.Flow

interface LikesRepository {

    /** Image ids liked by the user on this device (local, DataStore). */
    val likedIds: Flow<Set<String>>

    /** Reads every like counter with a single Firestore request. */
    suspend fun getLikeCounts(): Result<Map<String, Long>>

    /**
     * Toggles the like of [imageId]: likes it if it was not liked, otherwise removes the like.
     * Returns the new state (true = liked).
     */
    suspend fun toggleLike(imageId: String): Result<Boolean>
}
