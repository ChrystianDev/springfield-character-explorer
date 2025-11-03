/**
 * Created by chrystian.lozada.local (13/10/25)
 */
package com.mobilitydevai.sce.domain.usecase
import com.mobilitydevai.sce.domain.entity.LocationsSimpsonsEntity
import com.mobilitydevai.sce.domain.repository.SimpsonsRepository
import javax.inject.Inject

class LocationsUsecase @Inject constructor(
    private val repository: SimpsonsRepository
) {
    suspend operator fun invoke(page: String): LocationsSimpsonsEntity? {
        return repository.fetchLocations(page = page)
    }
}