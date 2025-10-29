package com.example.simpsonsinfo.domain.entity

data class CharacterEntity(
    val id: Int = 0,
    val age: Int? = null,
    val birthdate: String? = null,
    val gender: String = "",
    val name: String = "",
    val occupation: String? = null,
    val portrait_path: String = "",
    val phrases: List<String> = emptyList(),
    val status: String = "",
) {

    fun toFavouriteCharacterEntity(): FavouriteCharacterEntity {
        return FavouriteCharacterEntity(
            fid = id,
            age = age,
            birthdate = birthdate,
            gender = gender,
            name = name,
            occupation = occupation,
            urlImage = portrait_path,
            phrases = phrases,
            status = status,
        )
    }

}
