/**
 * Created by chrystian.lozada.local (20/11/25)
 */
package com.mobilitydevai.sce.domain.entity

data class EpisodeEntity(
    val id: Int = 0,
    val airdate: String = "",
    val episode_number: Int = 0,
    val image_path: String = "",
    val name: String = "",
    val season: Int = 0,
    val synopsis: String = ""
)
