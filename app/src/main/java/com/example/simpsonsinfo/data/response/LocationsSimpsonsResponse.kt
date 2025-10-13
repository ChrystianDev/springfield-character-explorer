package com.example.simpsonsinfo.data.response

import com.example.simpsonsinfo.domain.entity.LocationsSimpsonsEntity
import com.google.gson.annotations.SerializedName

data class LocationsSimpsonsResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String?,
    @SerializedName("prev")
    val prev: String?,
    @SerializedName("pages")
    val pages: Int?,
    @SerializedName("results")
    val results: List<LocationResponse>,
)

fun LocationsSimpsonsResponse.toDomain(): LocationsSimpsonsEntity {
    return LocationsSimpsonsEntity(
        count = count,
        next = next ?: "",
        prev = prev ?: "",
        pages = pages ?: 0,
        results = results.map { locationResponse ->
            locationResponse.toDomain()
        }
    )
}