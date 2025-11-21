/**
 * Created by chrystian.lozada.local (21/11/25)
 */
package com.mobilitydevai.sce.data.repository.remote

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.mobilitydevai.sce.domain.entity.ImageEntity
import com.mobilitydevai.sce.domain.entity.VoteEntity
import com.mobilitydevai.sce.domain.repository.ImageRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ImageRepository {

    companion object {
        private const val IMAGES_COLLECTION = "images-sce"
        private const val VOTES_COLLECTION = "images-vote"
        private const val VOTES_SUBCOLLECTION = "votes"

        private const val FIELD_URL = "url"
        private const val FIELD_DESCRIPTION = "description"
        private const val FIELD_LIKE_COUNT = "likeCount"
        private const val FIELD_UNLIKE_COUNT = "unlikeCount"
        private const val FIELD_TIMESTAMP = "timestamp"

        private const val FIELD_VOTE = "vote"
        private const val VOTE_LIKE = "like"
        private const val VOTE_UNLIKE = "unlike"
    }

    /**
     * Formats the location ID as imgXXX (e.g., 1 -> img001, 12 -> img012, 123 -> img123)
     */
    private fun formatImageId(id: Int): String {
        return "img${id.toString().padStart(3, '0')}"
    }

    override suspend fun getImage(imageId: String): Result<ImageEntity?> {
        return try {
            val document = firestore.collection(IMAGES_COLLECTION)
                .document(imageId)
                .get()
                .await()

            if (document.exists()) {
                val image = ImageEntity(
                    imageId = imageId,
                    url = document.getString(FIELD_URL) ?: "",
                    description = document.getString(FIELD_DESCRIPTION) ?: "",
                    likeCount = document.getLong(FIELD_LIKE_COUNT) ?: 0,
                    unlikeCount = document.getLong(FIELD_UNLIKE_COUNT) ?: 0,
                    timestamp = document.getString(FIELD_TIMESTAMP) ?: ""
                )
                Result.success(image)
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createOrUpdateImage(image: ImageEntity): Result<Unit> {
        return try {
            val imageMap = hashMapOf(
                FIELD_URL to image.url,
                FIELD_DESCRIPTION to image.description,
                FIELD_LIKE_COUNT to image.likeCount,
                FIELD_UNLIKE_COUNT to image.unlikeCount,
                FIELD_TIMESTAMP to image.timestamp
            )

            firestore.collection(IMAGES_COLLECTION)
                .document(image.imageId)
                .set(imageMap, SetOptions.merge())
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserVote(imageId: String, userId: String): Result<VoteEntity?> {
        return try {
            val document = firestore.collection(VOTES_COLLECTION)
                .document(imageId)
                .collection(VOTES_SUBCOLLECTION)
                .document(userId)
                .get()
                .await()

            if (document.exists()) {
                val vote = VoteEntity(
                    userId = userId,
                    vote = document.getString(FIELD_VOTE) ?: "",
                    timestamp = document.getString(FIELD_TIMESTAMP) ?: ""
                )
                Result.success(vote)
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addOrUpdateVote(
        imageId: String,
        userId: String,
        vote: String
    ): Result<Unit> {
        return try {
            // Get the current vote if exists
            val currentVoteResult = getUserVote(imageId, userId)
            val currentVote = currentVoteResult.getOrNull()

            // Start a batch write for atomic operations
            val batch = firestore.batch()

            val imageRef = firestore.collection(IMAGES_COLLECTION).document(imageId)
            val voteRef = firestore.collection(VOTES_COLLECTION)
                .document(imageId)
                .collection(VOTES_SUBCOLLECTION)
                .document(userId)

            // Update vote counts based on previous and new vote
            when {
                currentVote == null -> {
                    // New vote - increment the appropriate counter
                    if (vote == VOTE_LIKE) {
                        batch.update(imageRef, FIELD_LIKE_COUNT, FieldValue.increment(1))
                    } else {
                        batch.update(imageRef, FIELD_UNLIKE_COUNT, FieldValue.increment(1))
                    }
                }
                currentVote.vote != vote -> {
                    // Changed vote - decrement old and increment new
                    if (currentVote.vote == VOTE_LIKE) {
                        batch.update(imageRef, FIELD_LIKE_COUNT, FieldValue.increment(-1))
                        batch.update(imageRef, FIELD_UNLIKE_COUNT, FieldValue.increment(1))
                    } else {
                        batch.update(imageRef, FIELD_UNLIKE_COUNT, FieldValue.increment(-1))
                        batch.update(imageRef, FIELD_LIKE_COUNT, FieldValue.increment(1))
                    }
                }
                // If vote is the same, do nothing
            }

            // Save or update the vote
            val voteMap = hashMapOf(
                FIELD_VOTE to vote,
                FIELD_TIMESTAMP to System.currentTimeMillis().toString()
            )
            batch.set(voteRef, voteMap)

            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeVote(imageId: String, userId: String): Result<Unit> {
        return try {
            // Get the current vote
            val currentVoteResult = getUserVote(imageId, userId)
            val currentVote = currentVoteResult.getOrNull()

            if (currentVote != null) {
                val batch = firestore.batch()

                val imageRef = firestore.collection(IMAGES_COLLECTION).document(imageId)
                val voteRef = firestore.collection(VOTES_COLLECTION)
                    .document(imageId)
                    .collection(VOTES_SUBCOLLECTION)
                    .document(userId)

                // Decrement the appropriate counter
                if (currentVote.vote == VOTE_LIKE) {
                    batch.update(imageRef, FIELD_LIKE_COUNT, FieldValue.increment(-1))
                } else {
                    batch.update(imageRef, FIELD_UNLIKE_COUNT, FieldValue.increment(-1))
                }

                // Delete the vote document
                batch.delete(voteRef)

                batch.commit().await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
