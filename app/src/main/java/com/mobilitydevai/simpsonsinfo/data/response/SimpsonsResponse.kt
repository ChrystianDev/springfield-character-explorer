package com.mobilitydevai.simpsonsinfo.data.response

import com.mobilitydevai.simpsonsinfo.domain.entity.SimpsonsEntity
import com.google.gson.annotations.SerializedName

/**
 * [characters] debe ser el nombre de la key que me devuelve la peticion http,
 * para el el convertidor GJson a Kotlin lo encuentre y haga match con el
 * nombre de la variable
 *  {
 *      "characters": "https://thesimpsonsapi.com/api/characters",
 *      "episodes": "https://thesimpsonsapi.com/api/episodes",
 *      "locations": "https://thesimpsonsapi.com/api/locations"
 *  }
 */
//data class SimpsonsResponse(val characters: String)

/**
 * Con "@SerializedName" podemos dar otro nombre a nuestra variable
 *  {
 *      "characters": "https://thesimpsonsapi.com/api/characters",
 *      "episodes": "https://thesimpsonsapi.com/api/episodes",
 *      "locations": "https://thesimpsonsapi.com/api/locations"
 *  }
 */
//data class SimpsonsResponse(@SerializedName ("characters") val personajes: String)
data class SimpsonsResponse(
    @SerializedName ("count")
//    val count: Int,
    val contador: Int,
    @SerializedName ("next")
//    val next: String?,
    val siguiente: String?,
    @SerializedName ("prev")
//    val prev: String?,
    val anterior: String?,
    @SerializedName ("pages")
//    val pages: Int?,
    val paginas: Int?,
    @SerializedName ("results")
    val results: List<CharacterResponse>,
    )

fun SimpsonsResponse.toDomain() : SimpsonsEntity {
    return SimpsonsEntity(
//        count = count,
//        next = next ?: "",
//        prev = prev ?: "",
//        pages = pages ?: 0,
        count = contador,
        next = siguiente ?: "",
        prev = anterior ?: "",
        pages = paginas ?: 0,
        results = results.map { characterResponse ->
            characterResponse.toDomain()
        }
    )
}