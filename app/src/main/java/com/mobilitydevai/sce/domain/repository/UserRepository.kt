/**
 * Created by chrystian.lozada.local (20/11/25)
 */
package com.mobilitydevai.sce.domain.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.mobilitydevai.sce.domain.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUser(uid: String): Result<UserEntity?>
    suspend fun createUser(user: UserEntity): Result<Unit>
    suspend fun getCurrentUserUid(): String?
    suspend fun signInWithGoogle(account: GoogleSignInAccount): Result<UserEntity>
    suspend fun signOut(): Result<Unit>
    fun getCurrentUser(): Flow<UserEntity?>
    fun isUserAuthenticated(): Flow<Boolean>
}
