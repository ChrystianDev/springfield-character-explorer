package com.example.simpsonsinfo.domain.entity

data class CharacterEntity(
    val id: Int,
    val age: Int?,
    val birthdate: String?,
    val gender: String,
    val name: String,
    val occupation: String?,
    val portrait_path: String,
    val phrases: List<String>,
    val status: String,
)
