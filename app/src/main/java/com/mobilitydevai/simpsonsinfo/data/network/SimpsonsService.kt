/**
 * Created by chrystian.lozada.local (8/10/25)
 */
package com.mobilitydevai.simpsonsinfo.data.network

import com.mobilitydevai.simpsonsinfo.data.response.LocationsSimpsonsResponse
import com.mobilitydevai.simpsonsinfo.data.response.SimpsonsResponse
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
        }
    }

    suspend fun fetchLocations(page: String): Response<LocationsSimpsonsResponse> {
        return withContext(Dispatchers.IO) {
            simpsonsClient.fetchLocations(page = page)
        }
    }
}