/**
 * Created by chrystian.lozada.local (20/11/25)
 */
package com.mobilitydevai.sce.data.repository

import android.util.Log
import com.mobilitydevai.sce.data.network.EpisodesOkHttpClient
import com.mobilitydevai.sce.data.response.toDomain
import com.mobilitydevai.sce.domain.entity.EpisodesEntity
import com.mobilitydevai.sce.domain.repository.EpisodesRepository
import javax.inject.Inject

class EpisodesRepositoryImpl @Inject constructor(
    private val okHttpClient: EpisodesOkHttpClient
) : EpisodesRepository {

    override suspend fun fetchEpisodes(page: Int): EpisodesEntity? {
        return try {
            val response = okHttpClient.fetchEpisodes(page = page)
            response?.toDomain()
        } catch (e: Exception) {
            Log.e("fetchEpisodes", e.message.toString())
            null
        }
    }
}
