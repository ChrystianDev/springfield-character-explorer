/**
 * Created by chrystian.lozada.local (20/11/25)
 */
package com.mobilitydevai.sce.domain.repository

import com.mobilitydevai.sce.domain.entity.SeasonEntity

interface EpisodesRepository {
    /**
     * The API has no seasons endpoint, so all episode pages are fetched
     * and grouped by season (with their episodes). Returns null if any page fails.
     */
    suspend fun fetchSeasons(): List<SeasonEntity>?
}
