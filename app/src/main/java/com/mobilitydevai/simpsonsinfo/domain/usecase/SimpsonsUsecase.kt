/**
 * Created by chrystian.lozada.local (8/10/25)
 */
package com.mobilitydevai.simpsonsinfo.domain.usecase
import com.mobilitydevai.simpsonsinfo.domain.entity.SimpsonsEntity
import com.mobilitydevai.simpsonsinfo.domain.repository.SimpsonsRepository
import javax.inject.Inject

class SimpsonsUsecase @Inject constructor(
    private val repository: SimpsonsRepository
) {
    suspend operator fun invoke(page: String): SimpsonsEntity? {
        return repository.doSimpsons(page = page)
    }
}