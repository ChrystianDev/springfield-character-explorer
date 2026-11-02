/**
 * Created by chrystian.lozada.local (20/11/25)
 */
package com.mobilitydevai.sce.data.response

import com.google.gson.annotations.SerializedName
import com.mobilitydevai.sce.domain.entity.EpisodeEntity

data class EpisodeResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("airdate") val airdate: String,
    @SerializedName("episode_number") val episode_number: Int,
    @SerializedName("image_path") val image_path: String,
    @SerializedName("name") val name: String,
    @SerializedName("season") val season: Int,
    @SerializedName("synopsis") val synopsis: String
)

fun EpisodeResponse.toDomain(): EpisodeEntity {
    return EpisodeEntity(
        id = id,
        airdate = airdate,
        episode_number = episode_number,
        image_path = image_path,
        name = name,
        season = season,
        synopsis = synopsis
    )
}
