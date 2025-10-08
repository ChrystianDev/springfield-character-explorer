package com.example.simpsonsinfo.data.network

import com.example.simpsonsinfo.data.response.SimpsonsResponse
import retrofit2.Response
import retrofit2.http.GET

interface SimpsonsClient {
    @GET("characters")
    suspend fun doSimpsons(): Response<SimpsonsResponse>
}

