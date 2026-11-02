/**
 * Created by chrystian.lozada.local (20/11/25)
 */
package com.mobilitydevai.sce.data.repository

import android.util.Log
import com.mobilitydevai.sce.data.network.EpisodesOkHttpClient
import com.mobilitydevai.sce.data.response.toDomain
import com.mobilitydevai.sce.domain.entity.EpisodesEntity
import com.mobilitydevai.sce.domain.entity.SeasonEntity
import com.mobilitydevai.sce.domain.repository.EpisodesRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
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

    override suspend fun fetchSeasons(): List<SeasonEntity>? {
        return try {
            val firstPage = okHttpClient.fetchEpisodes(page = 1) ?: return null
            val otherPages = coroutineScope {
                (2..firstPage.pages).map { page ->
                    async { okHttpClient.fetchEpisodes(page = page) }
                }.awaitAll()
            }
            // An incomplete set of pages would give wrong episode counts
            if (otherPages.any { it == null }) return null

            (listOf(firstPage) + otherPages.filterNotNull())
                .flatMap { it.results }
                .map { it.toDomain() }
                .groupBy { it.season }
                .map { (season, episodes) ->
                    SeasonEntity(number = season, episodes = episodes.sortedBy { it.episode_number })
                }
                .sortedBy { it.number }
        } catch (e: Exception) {
            Log.e("fetchSeasons", e.message.toString())
            null
        }
    }
}
