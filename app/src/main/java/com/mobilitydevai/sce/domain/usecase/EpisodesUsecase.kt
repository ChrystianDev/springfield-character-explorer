/**
 * Created by chrystian.lozada.local (20/11/25)
 */
package com.mobilitydevai.sce.domain.usecase

import com.mobilitydevai.sce.domain.entity.EpisodesEntity
import com.mobilitydevai.sce.domain.repository.EpisodesRepository
import javax.inject.Inject

class EpisodesUsecase @Inject constructor(
    private val repository: EpisodesRepository
) {
    suspend operator fun invoke(page: Int): EpisodesEntity? {
        return repository.fetchEpisodes(page = page)
    }
}
