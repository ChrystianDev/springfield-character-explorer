package com.example.simpsonsinfo.domain.repository

import com.example.simpsonsinfo.domain.entity.SimpsonsEntity

interface SimpsonsRepository {
    suspend fun doSimpsons(name: String, description: String): SimpsonsEntity?
}