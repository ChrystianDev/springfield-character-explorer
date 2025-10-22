package com.example.simpsonsinfo.view.location

import com.example.simpsonsinfo.domain.entity.LocationEntity
import com.example.simpsonsinfo.domain.entity.LocationsSimpsonsEntity
import com.example.simpsonsinfo.domain.usecase.LocationsUsecase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
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

    val entityMock = LocationsSimpsonsEntity(
        count = 100,
        prev = "",
        next = "",
        pages = 10,
        results = listOf(
            LocationEntity(
                id = 1,
                name = "Springfield",
                imagePath = "photo.webp",
                town = "Springfield",
                use = "Place"
            ),
            LocationEntity(
                id = 2,
                name = "Mou's Bar",
                imagePath = "photo-1.webp",
                town = "Springfield",
                use = "Leisure"
            )
        )
    )


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
        coEvery { locationsUsecase(page = "1") } returns entityMock

        // WHEN
        locationsViewModel
        advanceUntilIdle()

        // THEN
        assert(locationsViewModel.uiState.value.entity.results == entityMock.results)
    }

    @Test
    fun toggleLoading() {
        setDispatcherAndViewModel()
        assert(!locationsViewModel.isLoading.value)
        locationsViewModel.toggleLoading()
        assert(locationsViewModel.isLoading.value)
        locationsViewModel.toggleLoading()
        assert(!locationsViewModel.isLoading.value)
    }


    @Test
    fun addLike() = runTest {
        val like = 1
        getLocations()
        assert(locationsViewModel.likesList.value[0].like == 0)
        locationsViewModel.updateLikes(index = 0, like = like)
        assert(locationsViewModel.likesList.value[0].like == 1)
    }

    @Test
    fun removeLike() = runTest {
        val like = -1
        // Add elements to list
        getLocations()
        // First add like to item
        locationsViewModel.updateLikes(index = 0, like = 1)
        // Check like
        assert(locationsViewModel.likesList.value[0].like == 1)

        // Remove like to item
        locationsViewModel.updateLikes(index = 0, like = like)
        // Check like
        assert(locationsViewModel.likesList.value[0].like == 0)
    }

    @Test
    fun incrementPage(){
        setDispatcherAndViewModel()
        val page = "2"
        assert(locationsViewModel.page.value == "1")
        locationsViewModel.incrementPage()
        assert(locationsViewModel.page.value == page)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when fetching locations, likes list is generated`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        locationsViewModel = LocationsViewModel(locationsUsecase, dispatcher)

        // Given
        coEvery { locationsUsecase(page = "1") } returns entityMock

        // When
        locationsViewModel
        advanceUntilIdle()

        // Then
        assert(locationsViewModel.likesList.value.size == entityMock.results.size)
        assert(locationsViewModel.likesList.value[0].like == 0)
        assert(locationsViewModel.likesList.value[0].index == 0)
    }

    fun setDispatcherAndViewModel(){
        dispatcher = StandardTestDispatcher()
        locationsViewModel = LocationsViewModel(locationsUsecase, dispatcher)
    }

}