/**
 * Created by chrystian.lozada.local (21/11/25)
 */
package com.mobilitydevai.sce.domain.repository

import com.mobilitydevai.sce.domain.entity.ImageEntity
import com.mobilitydevai.sce.domain.entity.VoteEntity

interface ImageRepository {
    suspend fun getImage(imageId: String): Result<ImageEntity?>
    suspend fun createOrUpdateImage(image: ImageEntity): Result<Unit>
    suspend fun getUserVote(imageId: String, userId: String): Result<VoteEntity?>
    suspend fun addLike(imageId: String, userId: String): Result<Unit>
    suspend fun removeVote(imageId: String, userId: String): Result<Unit>
}
