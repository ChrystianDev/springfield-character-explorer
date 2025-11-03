package com.mobilitydevai.simpsonsinfo.data.response

import com.mobilitydevai.simpsonsinfo.domain.entity.CharacterEntity
import com.google.gson.annotations.SerializedName

data class CharacterResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("age") val age: Int?,
    @SerializedName("birthdate") val birthdate: String?,
    @SerializedName("gender") val gender: String,
    @SerializedName("name") val name: String,
    @SerializedName("occupation") val occupation: String,
    @SerializedName("portrait_path") val portrait_path: String,
    @SerializedName("phrases") val phrases: List<String>,
    @SerializedName("status") val status: String,
)

fun CharacterResponse.toDomain() : CharacterEntity {
    return CharacterEntity(
        id = id,
        age = age,
        birthdate = birthdate,
        gender = gender,
        name = name,
        occupation = occupation,
        portrait_path = portrait_path,
        phrases = phrases,
        status = status,
    )
}