package com.example.simpsonsinfo.domain.repository

import com.example.simpsonsinfo.domain.entity.LocationsSimpsonsEntity
import com.example.simpsonsinfo.domain.entity.SimpsonsEntity

interface SimpsonsRepository {
    suspend fun doSimpsons(page: String): SimpsonsEntity?

    suspend fun fetchLocations(): LocationsSimpsonsEntity?
}