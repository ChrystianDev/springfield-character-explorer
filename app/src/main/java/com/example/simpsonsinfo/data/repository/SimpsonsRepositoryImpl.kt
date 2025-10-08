/**
 * Created by chrystian.lozada.local (8/10/25)
 */
package com.example.simpsonsinfo.data.repository

import com.example.simpsonsinfo.data.network.SimpsonsService
import com.example.simpsonsinfo.domain.repository.SimpsonsRepository
import javax.inject.Inject

class SimpsonsRepositoryImpl @Inject constructor(private val api: SimpsonsService) : SimpsonsRepository {

    override suspend fun doSimpsons(name: String, description: String): String {
        return api.doSimpsons(name, description)
    }
}