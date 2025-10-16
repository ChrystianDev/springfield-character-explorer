/**
 * Created by chrystian.lozada.local (13/10/25)
 */
package com.example.simpsonsinfo.domain.usecase
import com.example.simpsonsinfo.domain.entity.LocationsSimpsonsEntity
import com.example.simpsonsinfo.domain.repository.SimpsonsRepository
import javax.inject.Inject

class LocationsUsecase @Inject constructor(
    private val repository: SimpsonsRepository
) {
    suspend operator fun invoke(page: String): LocationsSimpsonsEntity? {
        return repository.fetchLocations(page = page)
    }
}