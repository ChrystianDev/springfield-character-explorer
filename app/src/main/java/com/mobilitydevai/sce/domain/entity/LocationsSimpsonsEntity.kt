package com.mobilitydevai.sce.domain.entity

data class LocationsSimpsonsEntity(
    val count: Int,
    val next: String?,
    val prev: String?,
    val pages: Int?,
    val results: List<LocationEntity>,
)
