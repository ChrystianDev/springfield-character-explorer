package com.mobilitydevai.sce.data.response

import com.mobilitydevai.sce.domain.entity.LocationEntity
import com.google.gson.annotations.SerializedName

data class LocationResponse(

    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("image_path") val image_path: String,
    @SerializedName("town") val town: String,
    @SerializedName("use") val use: String,
)

fun LocationResponse.toDomain() : LocationEntity {
    return LocationEntity(
        id = id,
        name = name,
        imagePath = image_path,
        town = town,
        use = use,
    )
}
