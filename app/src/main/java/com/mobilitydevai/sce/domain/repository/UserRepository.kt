/**
 * Created by chrystian.lozada.local (20/11/25)
 */
package com.mobilitydevai.sce.domain.repository

import com.mobilitydevai.sce.domain.entity.UserEntity

interface UserRepository {
    suspend fun getUser(uid: String): Result<UserEntity?>
    suspend fun createUser(user: UserEntity): Result<Unit>
    suspend fun getCurrentUserUid(): String?

    /**
     * Returns the uid of the current Firebase user, signing in anonymously first
     * if there is no session. One anonymous uid per app installation.
     */
    suspend fun getOrCreateAnonymousUid(): Result<String>
}
