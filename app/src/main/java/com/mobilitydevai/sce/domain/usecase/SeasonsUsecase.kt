/**
 * Created by chrystian.lozada.local (02/11/26)
 */
package com.mobilitydevai.sce.domain.usecase

import com.mobilitydevai.sce.domain.entity.SeasonEntity
import com.mobilitydevai.sce.domain.repository.EpisodesRepository
import javax.inject.Inject

class SeasonsUsecase @Inject constructor(
    private val repository: EpisodesRepository
) {
    suspend operator fun invoke(): List<SeasonEntity>? {
        return repository.fetchSeasons()
    }
}
