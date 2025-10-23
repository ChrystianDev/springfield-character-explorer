package com.example.simpsonsinfo.view.location

import com.example.simpsonsinfo.domain.usecase.LocationsUsecase
import com.example.simpsonsinfo.utils.mock.LocationMocks.aLocationsSimpsonsEntityMock
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class LocationsViewModelTest {

    @RelaxedMockK
    private lateinit var locationsUsecase: LocationsUsecase

    private lateinit var locationsViewModel: LocationsViewModel

    private lateinit var dispatcher: TestDispatcher


    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getLocations() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        locationsViewModel = LocationsViewModel(locationsUsecase, dispatcher)

        // GIVEN
        coEvery { locationsUsecase(page = "1") } returns aLocationsSimpsonsEntityMock()

        // WHEN
        locationsViewModel
        advanceUntilIdle()

        // THEN
        assertEquals(true, locationsViewModel.uiState.value.entity.results == aLocationsSimpsonsEntityMock().results)
    }

    @Test
    fun toggleLoading() {
        setDispatcherAndViewModel()
        assertEquals(false, locationsViewModel.isLoading.value)
        locationsViewModel.toggleLoading()
        assertEquals(true, locationsViewModel.isLoading.value)
        locationsViewModel.toggleLoading()
        assertEquals(false, locationsViewModel.isLoading.value)
    }


    @Test
    fun incrementLike() = runTest {
        val like = 1
        getLocations()
        assertEquals(0, locationsViewModel.likesList.value[0].like)
        locationsViewModel.updateLikes(index = 0, like = like)
        assertEquals(1, locationsViewModel.likesList.value[0].like)
    }

    @Test
    fun decrementLike() = runTest {
        val like = -1
        // Add elements to list
        getLocations()
        // First add like to item
        locationsViewModel.updateLikes(index = 0, like = 1)
        // Check like
        assertEquals(1, locationsViewModel.likesList.value[0].like)

        // Remove like to item
        locationsViewModel.updateLikes(index = 0, like = like)
        // Check like
        assertEquals(0, locationsViewModel.likesList.value[0].like)
    }

    @Test
    fun incrementPage() {
        setDispatcherAndViewModel()
        assertEquals("1", locationsViewModel.page.value)

        // Increment page from 1 to 2
        locationsViewModel.incrementPage()
        val page = "2"
        assertEquals(page, locationsViewModel.page.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when fetching locations, likes list is generated`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        locationsViewModel = LocationsViewModel(locationsUsecase, dispatcher)

        // Given
        coEvery { locationsUsecase(page = "1") } returns aLocationsSimpsonsEntityMock()

        // When
        locationsViewModel
        advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { locationsUsecase(page = "1") }
        assertEquals(true, locationsViewModel.likesList.value.size == aLocationsSimpsonsEntityMock().results.size)
        assertEquals(0, locationsViewModel.likesList.value[0].like)
        assertEquals(0, locationsViewModel.likesList.value[0].index)
    }

    fun setDispatcherAndViewModel() {
        dispatcher = StandardTestDispatcher()
        locationsViewModel = LocationsViewModel(locationsUsecase, dispatcher)
    }
}