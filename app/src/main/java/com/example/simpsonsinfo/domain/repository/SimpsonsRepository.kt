package com.example.simpsonsinfo.domain.repository

interface SimpsonsRepository {
    suspend fun doSimpsons(name: String, description: String): String
}