package com.mobilitydevai.sce.domain.repository

import com.mobilitydevai.sce.domain.entity.LocationsSimpsonsEntity
import com.mobilitydevai.sce.domain.entity.SimpsonsEntity

interface SimpsonsRepository {
    suspend fun doSimpsons(page: String): SimpsonsEntity?

    suspend fun fetchLocations(page: String): LocationsSimpsonsEntity?
}