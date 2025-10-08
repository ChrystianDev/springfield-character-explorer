/**
 * Created by chrystian.lozada.local (8/10/25)
 */
package com.example.simpsonsinfo.domain.usecase
import com.example.simpsonsinfo.domain.repository.SimpsonsRepository
import javax.inject.Inject

class SimpsonsUsecase @Inject constructor(
    private val repository: SimpsonsRepository
) {
    suspend operator fun invoke(name: String, description: String): String {
        return repository.doSimpsons(name, description)
    }
}