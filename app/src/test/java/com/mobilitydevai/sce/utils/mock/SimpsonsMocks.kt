package com.mobilitydevai.sce.utils.mock

import com.mobilitydevai.sce.domain.entity.CharacterEntity
import com.mobilitydevai.sce.domain.entity.SimpsonsEntity

object SimpsonsMocks {
    fun aSimpsonsEntityMock(
        count: Int = 1182,
        next: String = "https://thesimpsonsapi.com/api/characters?page=n",
        prev: String = "https://thesimpsonsapi.com/api/characters?page=m",
        pages: Int = 60,
        results: List<CharacterEntity> = listOf(
            aCharacterEntityMock(),
            aCharacterEntityMock(
                id = 2,
                age = 40,
                gender = "Female",
                name = "Marge Simpson",
                occupation = "Housewife",
                status = "Alive"
            ),
            aCharacterEntityMock(
                id = 3,
                age = 12,
                gender = "Male",
                name = "Bart Simpson",
                occupation = "Student",
                status = "Alive"
            )
        )
    ) = SimpsonsEntity(count, next, prev, pages, results = results)

    fun aSimpsonsEmptyListEntityMock(
        count: Int = 0,
        next: String = "",
        prev: String = "",
        pages: Int = 0,
        results: List<CharacterEntity> = emptyList()
    ) = SimpsonsEntity(count, next, prev, pages, results)

    fun aCharacterEntityMock(
        id: Int = 1,
        age: Int = 44,
        birthdate: String = "01/01/1991",
        gender: String = "Male",
        name: String = "Homer",
        occupation: String = "Secure Inspector",
        portraitPath: String = "photo.webp",
        phrases: List<String> = listOf("Hi", "My name is ..."),
        status: String = "Alive"
    ) = CharacterEntity(
        id,
        age,
        birthdate,
        gender,
        name,
        occupation,
        portraitPath,
        phrases,
        status
    )
}