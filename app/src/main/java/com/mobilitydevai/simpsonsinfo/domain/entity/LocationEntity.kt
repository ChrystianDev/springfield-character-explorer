/**
 * Created by chrystian.lozada.local (13/10/25)
 */
package com.mobilitydevai.simpsonsinfo.domain.entity

data class LocationEntity(
    val id: Int,
    val name: String,
    val imagePath: String,
    val town: String,
    val use: String,
)
