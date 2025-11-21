/**
 * Created by chrystian.lozada.local (20/11/25)
 */
package com.mobilitydevai.sce.data.repository.remote

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.mobilitydevai.sce.domain.entity.UserEntity
import com.mobilitydevai.sce.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : UserRepository {

    companion object {
        private const val USERS_COLLECTION = "locations"
        private const val FIELD_UID = "uid"
        private const val FIELD_NAME = "name"
        private const val FIELD_EMAIL = "email"
        private const val FIELD_LIKE = "like"
        private const val FIELD_UNLIKE = "unlike"
        private const val FIELD_IMAGE_ID = "imageId"
    }

    override suspend fun getUser(uid: String): Result<UserEntity?> {
        return try {
            val document = firestore.collection(USERS_COLLECTION)
                .document(uid)
                .get()
                .await()

            if (document.exists()) {
                val user = UserEntity(
                    uid = document.getString(FIELD_UID) ?: "",
                    name = document.getString(FIELD_NAME) ?: "",
                    email = document.getString(FIELD_EMAIL) ?: "",
                    like = document.getLong(FIELD_LIKE) ?: 0,
                    unlike = document.getLong(FIELD_UNLIKE) ?: 0,
                    imageId = document.getLong(FIELD_IMAGE_ID) ?: 0
                )
                Result.success(user)
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createUser(user: UserEntity): Result<Unit> {
        return try {
            val userMap = hashMapOf(
                FIELD_UID to user.uid,
                FIELD_NAME to user.name,
                FIELD_EMAIL to user.email,
                FIELD_LIKE to user.like,
                FIELD_UNLIKE to user.unlike,
                FIELD_IMAGE_ID to user.imageId
            )

            firestore.collection(USERS_COLLECTION)
                .document(user.uid)
                .set(userMap)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateLikes(uid: String, increment: Boolean): Result<Unit> {
        return try {
            val value = if (increment) FieldValue.increment(1) else FieldValue.increment(-1)

            firestore.collection(USERS_COLLECTION)
                .document(uid)
                .update(FIELD_LIKE, value)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUnlikes(uid: String, increment: Boolean): Result<Unit> {
        return try {
            val value = if (increment) FieldValue.increment(1) else FieldValue.increment(-1)

            val document = hashMapOf(
                "name" to "John Doe",
                "email" to "johndoe@gmail.com",
                "uid" to "lfkjafjf84aksl",
                "like" to 1,
                "unlike" to 0,
            )

            firestore.collection(USERS_COLLECTION)
                .document(uid)
                .set(document)
                .await()

//            firestore.collection(USERS_COLLECTION)
//                .document(uid)
//                .update(FIELD_UNLIKE, value)
//                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUserUid(): String? {
        Log.i("AUTH-USER", "getCurrentUserUid: ${auth.currentUser?.uid}")
//        return auth.currentUser?.uid
        return "sT6Gj9KzLxR0yA2bC4dE7fH8iJ3lM5nO"
    }
}
