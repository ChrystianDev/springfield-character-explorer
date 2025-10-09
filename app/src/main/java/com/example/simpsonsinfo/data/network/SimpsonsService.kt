/**
 * Created by chrystian.lozada.local (8/10/25)
 */
package com.example.simpsonsinfo.data.network

import com.example.simpsonsinfo.data.response.SimpsonsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class SimpsonsService @Inject constructor(
    private val simpsonsClient: SimpsonsClient
) {

    suspend fun doSimpsons(name: String, description: String): Response<SimpsonsResponse> {
        return withContext(Dispatchers.IO) {
            simpsonsClient.doSimpsons()
//            val response = simpsonsClient.doSimpsons()
//            response.body()?.personajes ?: "No hay personajes"
//            response.body()?.count ?: "No hay personajes"
        }
    }
}