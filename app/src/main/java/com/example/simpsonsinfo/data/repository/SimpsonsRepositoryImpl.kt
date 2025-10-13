/**
 * Created by chrystian.lozada.local (8/10/25)
 */
package com.example.simpsonsinfo.data.repository

import com.example.simpsonsinfo.data.network.SimpsonsService
import com.example.simpsonsinfo.data.response.toDomain
import com.example.simpsonsinfo.domain.entity.LocationsSimpsonsEntity
import com.example.simpsonsinfo.domain.entity.SimpsonsEntity
import com.example.simpsonsinfo.domain.repository.SimpsonsRepository
import javax.inject.Inject

class SimpsonsRepositoryImpl @Inject constructor(private val api: SimpsonsService) :
    SimpsonsRepository {

    override suspend fun doSimpsons(name: String, description: String): SimpsonsEntity? {
        try {
            val response = api.doSimpsons(name, description)
            return response.body()?.toDomain()
        } catch (e: Exception) {
            return null
        }

    }

    override suspend fun fetchLocations(): LocationsSimpsonsEntity? {
        try {
            val response = api.fetchLocations()
            return response.body()?.toDomain()
        }catch (e: Exception){
            return null
        }
    }
}
