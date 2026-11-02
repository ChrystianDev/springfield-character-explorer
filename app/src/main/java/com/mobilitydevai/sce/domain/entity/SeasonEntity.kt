/**
 * Created by chrystian.lozada.local (02/11/26)
 */
package com.mobilitydevai.sce.domain.entity

data class SeasonEntity(
    val number: Int = 0,
    /** Sorted by episode number: index 0 is chapter 1 of the season */
    val episodes: List<EpisodeEntity> = emptyList()
) {
    val episodeCount: Int get() = episodes.size
}
