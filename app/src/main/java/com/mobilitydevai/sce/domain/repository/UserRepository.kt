/**
 * Created by chrystian.lozada.local (20/11/25)
 */
package com.mobilitydevai.sce.domain.repository

import com.mobilitydevai.sce.domain.entity.UserEntity

interface UserRepository {
    suspend fun getUser(uid: String): Result<UserEntity?>
    suspend fun createUser(user: UserEntity): Result<Unit>
    suspend fun updateLikes(uid: String, increment: Boolean): Result<Unit>
    suspend fun updateUnlikes(uid: String, increment: Boolean): Result<Unit>
    suspend fun getCurrentUserUid(): String?
}
