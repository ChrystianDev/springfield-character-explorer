/**
 * Created by chrystian.lozada.local (20/11/25)
 */
package com.mobilitydevai.sce.data.repository.remote

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
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
        private const val USERS_COLLECTION = "users"
        private const val FIELD_NAME = "name"
        private const val FIELD_EMAIL = "email"
        private const val FIELD_CREATED_AT = "createdAt"
    }

    override suspend fun getUser(uid: String): Result<UserEntity?> {
        return try {
            val document = firestore.collection(USERS_COLLECTION)
                .document(uid)
                .get()
                .await()

            if (document.exists()) {
                val user = UserEntity(
                    uid = uid,
                    name = document.getString(FIELD_NAME) ?: "",
                    email = document.getString(FIELD_EMAIL) ?: "",
                    createdAt = document.getString(FIELD_CREATED_AT) ?: ""
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
                FIELD_NAME to user.name,
                FIELD_EMAIL to user.email,
                FIELD_CREATED_AT to user.createdAt
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

    override suspend fun getCurrentUserUid(): String? {
        Log.i("AUTH-USER", "getCurrentUserUid: ${auth.currentUser?.uid}")
        return auth.currentUser?.uid
    }

    override suspend fun getOrCreateAnonymousUid(): Result<String> {
        return try {
            auth.currentUser?.uid?.let { return Result.success(it) }
            val uid = auth.signInAnonymously().await().user?.uid
                ?: return Result.failure(Exception("Anonymous sign-in failed: User is null"))
            Log.i("AUTH-USER", "signInAnonymously: $uid")
            Result.success(uid)
        } catch (e: Exception) {
            Log.e("AUTH-USER", "signInAnonymously failed", e)
            Result.failure(e)
        }
    }
}
