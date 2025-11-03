package com.mobilitydevai.simpsonsinfo.domain.repository

import com.mobilitydevai.simpsonsinfo.domain.entity.LocationsSimpsonsEntity
import com.mobilitydevai.simpsonsinfo.domain.entity.SimpsonsEntity

interface SimpsonsRepository {
    suspend fun doSimpsons(page: String): SimpsonsEntity?

    suspend fun fetchLocations(page: String): LocationsSimpsonsEntity?
}