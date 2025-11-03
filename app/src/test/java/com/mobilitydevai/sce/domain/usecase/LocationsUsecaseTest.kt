package com.mobilitydevai.sce.domain.usecase

import com.mobilitydevai.sce.domain.repository.SimpsonsRepository
import com.mobilitydevai.sce.utils.mock.LocationMocks.aLocationsSimpsonsEntityMock
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
        // Given
        coEvery { repository.fetchLocations("1") } returns aLocationsSimpsonsEntityMock()

        // When
        val response = locationsUsecase(page = "1")

        // Then
        coVerify(exactly = 1) { repository.fetchLocations(page = "1") }
        assert(response == aLocationsSimpsonsEntityMock())
    }
}
