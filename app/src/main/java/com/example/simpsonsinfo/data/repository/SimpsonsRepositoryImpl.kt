/**
 * Created by chrystian.lozada.local (8/10/25)
 */
package com.example.simpsonsinfo.data.repository

import android.util.Log
import com.example.simpsonsinfo.data.network.SimpsonsService
import com.example.simpsonsinfo.data.response.toDomain
import com.example.simpsonsinfo.domain.entity.LocationsSimpsonsEntity
import com.example.simpsonsinfo.domain.entity.SimpsonsEntity
import com.example.simpsonsinfo.domain.repository.SimpsonsRepository
import javax.inject.Inject

class SimpsonsRepositoryImpl @Inject constructor(private val api: SimpsonsService) :
    SimpsonsRepository {

    override suspend fun doSimpsons(page: String): SimpsonsEntity? {
        try {
            val response = api.doSimpsons(page = page)
            return response.body()?.toDomain()
        } catch (e: Exception) {
            Log.e("doSimpsons", e.message.toString())
            return null
        }

    }

    override suspend fun fetchLocations(page: String): LocationsSimpsonsEntity? {
        try {
            val response = api.fetchLocations(page = page)
            return response.body()?.toDomain()
        }catch (e: Exception){
            Log.e("fetchLocations", e.message.toString())
            return null
        }
    }
}
