/**
 * Created by chrystian.lozada.local (20/11/25)
 */
package com.mobilitydevai.sce.data.response

import com.google.gson.annotations.SerializedName
import com.mobilitydevai.sce.domain.entity.EpisodesEntity

data class EpisodesResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("prev") val prev: String?,
    @SerializedName("pages") val pages: Int,
    @SerializedName("results") val results: List<EpisodeResponse>
)

fun EpisodesResponse.toDomain(): EpisodesEntity {
    return EpisodesEntity(
        count = count,
        next = next ?: "",
        prev = prev ?: "",
        pages = pages,
        results = results.map { it.toDomain() }
    )
}
