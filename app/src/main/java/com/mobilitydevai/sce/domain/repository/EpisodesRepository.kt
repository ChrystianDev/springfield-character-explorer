/**
 * Created by chrystian.lozada.local (20/11/25)
 */
package com.mobilitydevai.sce.domain.repository

import com.mobilitydevai.sce.domain.entity.EpisodesEntity
import com.mobilitydevai.sce.domain.entity.SeasonEntity

interface EpisodesRepository {
    suspend fun fetchEpisodes(page: Int): EpisodesEntity?

    /**
     * The API has no seasons endpoint, so all episode pages are fetched
     * and grouped by season (with their episodes). Returns null if any page fails.
     */
    suspend fun fetchSeasons(): List<SeasonEntity>?
}
