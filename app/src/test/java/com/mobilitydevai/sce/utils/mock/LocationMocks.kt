package com.mobilitydevai.sce.utils.mock

import com.mobilitydevai.sce.domain.entity.LocationEntity
import com.mobilitydevai.sce.domain.entity.LocationsSimpsonsEntity

object LocationMocks {
    fun aLocationEntityMock(
        id: Int = 1,
        name: String = "Springfield",
        imagePath: String = "photo.webp",
        town: String = "Springfield",
        use: String = "Place"
    ) = LocationEntity(id, name, imagePath, town, use)


    fun aLocationsSimpsonsEntityMock(
        count: Int = 100,
        next: String = "",
        prev: String = "",
        pages: Int = 10,
        results: List<LocationEntity> = listOf(
            aLocationEntityMock(),
            aLocationEntityMock(
                id = 2,
                name = "Mou's Bar",
                use = "Leisure"
            ),
            aLocationEntityMock(
                id = 3,
                name = "Burns Manor",
                use = "Residential"
            )
        )
    ) = LocationsSimpsonsEntity(count, next, prev, pages, results = results)
}