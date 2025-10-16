package com.example.simpsonsinfo.data.network

import com.example.simpsonsinfo.data.response.LocationsSimpsonsResponse
import com.example.simpsonsinfo.data.response.SimpsonsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SimpsonsClient {
    @GET("characters")
    suspend fun doSimpsons(@Query("page") page: String): Response<SimpsonsResponse>

    @GET("locations")
    suspend fun fetchLocations(@Query("page") page: String): Response<LocationsSimpsonsResponse>
}

