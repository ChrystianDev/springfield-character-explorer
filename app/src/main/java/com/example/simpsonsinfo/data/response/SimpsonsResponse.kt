package com.example.simpsonsinfo.data.response

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
data class SimpsonsResponse(@SerializedName ("count") val count: String)
