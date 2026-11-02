/**
 * Created by chrystian.lozada.local (20/11/25)
 */
package com.mobilitydevai.sce.domain.entity

data class EpisodesEntity(
    val count: Int = 0,
    val next: String = "",
    val prev: String = "",
    val pages: Int = 0,
    val results: List<EpisodeEntity> = emptyList()
)
