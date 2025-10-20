package com.example.simpsonsinfo.domain.usecase

import com.example.simpsonsinfo.domain.entity.LocationsSimpsonsEntity
import com.example.simpsonsinfo.domain.repository.SimpsonsRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class LocationsUsecaseTest {

    @RelaxedMockK
    private lateinit var repository: SimpsonsRepository

    lateinit var locationsUsecase: LocationsUsecase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        locationsUsecase = LocationsUsecase(repository)
    }

    @Test
//    fun `when api doesn't return anything then get values from database`() = runBlocking {
    fun getLocationsEmpty() = runBlocking {
        // Given
        coEvery { repository.fetchLocations("1") } returns null

        // When
        val response = locationsUsecase(page = "1")

        // Then
        coVerify(exactly = 1) { repository.fetchLocations(page = "1") }
        assert(response == null)
    }

    @Test
    fun getLocations() = runBlocking {
        val entity = LocationsSimpsonsEntity(
            count = 100,
            prev = "",
            next = "",
            pages = 10,
            results = listOf()
        )
        // Given
        coEvery { repository.fetchLocations("1") } returns entity

        // When
        val response = locationsUsecase(page = "1")

        // Then
        coVerify(exactly = 1) { repository.fetchLocations(page = "1") }
        assert(response == entity)
    }
}
