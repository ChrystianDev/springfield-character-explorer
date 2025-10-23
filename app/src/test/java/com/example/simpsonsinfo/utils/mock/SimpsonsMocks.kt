package com.example.simpsonsinfo.utils.mock

import com.example.simpsonsinfo.domain.entity.CharacterEntity
import com.example.simpsonsinfo.domain.entity.SimpsonsEntity

object SimpsonsMocks {
    fun aSimpsonsEntityMock(
        count: Int = 0,
        next: String = "",
        prev: String = "",
        pages: Int = 0,
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