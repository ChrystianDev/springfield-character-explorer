/**
 * Created by chrystian.lozada.local (1/11/26)
 */
package com.mobilitydevai.sce.data.repository.remote

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.mobilitydevai.sce.domain.repository.LikesRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Firestore model: a single document `likes/locations` with one counter per image,
 * e.g. { "img001": 12, "img017": 3 }.
 * - Read all counters: 1 read.
 * - Like / unlike: 1 write (atomic FieldValue.increment, no previous read).
 *
 * Which images the user liked is stored only on the device (DataStore).
 */
class LikesRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val dataStore: DataStore<Preferences>,
) : LikesRepository {

    companion object {
        private const val LIKES_COLLECTION = "likes"
        private const val LOCATIONS_DOCUMENT = "locations"
        private val LIKED_IDS_KEY = stringSetPreferencesKey("liked_location_ids")
    }

    private val likesDocument
        get() = firestore.collection(LIKES_COLLECTION).document(LOCATIONS_DOCUMENT)

    override val likedIds: Flow<Set<String>> =
        dataStore.data.map { preferences -> preferences[LIKED_IDS_KEY] ?: emptySet() }

    override suspend fun getLikeCounts(): Result<Map<String, Long>> {
        return try {
            val snapshot = likesDocument.get().await()
            val counts = snapshot.data.orEmpty()
                .mapValues { (_, value) -> (value as? Number)?.toLong() ?: 0L }
            Result.success(counts)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleLike(imageId: String): Result<Boolean> {
        val wasLiked = imageId in likedIds.first()
        val nowLiked = !wasLiked

        // Local first so the icon updates instantly
        setLikedLocally(imageId, nowLiked)

        return try {
            likesDocument
                .set(
                    mapOf(imageId to FieldValue.increment(if (nowLiked) 1L else -1L)),
                    SetOptions.merge()
                )
                .await()
            Result.success(nowLiked)
        } catch (e: CancellationException) {
            setLikedLocally(imageId, wasLiked)
            throw e
        } catch (e: Exception) {
            // Revert local state if Firestore failed
            setLikedLocally(imageId, wasLiked)
            Result.failure(e)
        }
    }

    private suspend fun setLikedLocally(imageId: String, liked: Boolean) {
        dataStore.edit { preferences ->
            val current = preferences[LIKED_IDS_KEY] ?: emptySet()
            preferences[LIKED_IDS_KEY] = if (liked) current + imageId else current - imageId
        }
    }
}
