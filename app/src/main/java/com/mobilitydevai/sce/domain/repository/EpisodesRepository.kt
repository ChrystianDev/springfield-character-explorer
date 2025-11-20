/**
 * Created by chrystian.lozada.local (20/11/25)
 */
package com.mobilitydevai.sce.domain.repository

import com.mobilitydevai.sce.domain.entity.EpisodesEntity

interface EpisodesRepository {
    suspend fun fetchEpisodes(page: Int): EpisodesEntity?
}
