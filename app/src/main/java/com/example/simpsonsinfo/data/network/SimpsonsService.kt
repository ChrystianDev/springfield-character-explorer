/**
 * Created by chrystian.lozada.local (8/10/25)
 */
package com.example.simpsonsinfo.data.network

import com.example.simpsonsinfo.data.response.LocationsSimpsonsResponse
import com.example.simpsonsinfo.data.response.SimpsonsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class SimpsonsService @Inject constructor(
    private val simpsonsClient: SimpsonsClient
) {

    suspend fun doSimpsons(page: String): Response<SimpsonsResponse> {
        return withContext(Dispatchers.IO) {
            simpsonsClient.doSimpsons(page = page)
//            val response = simpsonsClient.doSimpsons()
//            response.body()?.personajes ?: "No hay personajes"
//            response.body()?.count ?: "No hay personajes"
        }
    }

    suspend fun fetchLocations(): Response<LocationsSimpsonsResponse> {
        return withContext(Dispatchers.IO) {
            simpsonsClient.fetchLocations()
        }
    }
}