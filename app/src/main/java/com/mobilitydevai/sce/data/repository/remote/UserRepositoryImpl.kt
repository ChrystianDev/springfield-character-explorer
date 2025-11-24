/**
 * Created by chrystian.lozada.local (20/11/25)
 */
package com.mobilitydevai.sce.data.repository.remote

import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.mobilitydevai.sce.domain.entity.UserEntity
import com.mobilitydevai.sce.domain.repository.UserRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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

    override suspend fun signInWithGoogle(account: GoogleSignInAccount): Result<UserEntity> {
        return try {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            val authResult = auth.signInWithCredential(credential).await()
            val firebaseUser = authResult.user

            if (firebaseUser != null) {
                val userEntity = UserEntity(
                    uid = firebaseUser.uid,
                    name = firebaseUser.displayName ?: "",
                    email = firebaseUser.email ?: "",
                    createdAt = System.currentTimeMillis().toString()
                )

                // Create or update user in Firestore
                createUser(userEntity)

                Result.success(userEntity)
            } else {
                Result.failure(Exception("Authentication failed: User is null"))
            }
        } catch (e: Exception) {
            Log.e("AUTH-USER", "signInWithGoogle failed", e)
            Result.failure(e)
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("AUTH-USER", "signOut failed", e)
            Result.failure(e)
        }
    }

    override fun getCurrentUser(): Flow<UserEntity?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser != null) {
                val userEntity = UserEntity(
                    uid = firebaseUser.uid,
                    name = firebaseUser.displayName ?: "",
                    email = firebaseUser.email ?: "",
                    createdAt = ""
                )
                trySend(userEntity)
            } else {
                trySend(null)
            }
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    override fun isUserAuthenticated(): Flow<Boolean> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser != null)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }
}
