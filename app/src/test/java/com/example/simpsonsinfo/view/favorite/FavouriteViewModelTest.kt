package com.example.simpsonsinfo.view.favorite

import com.example.simpsonsinfo.domain.entity.FavouriteCharacterEntity
import com.example.simpsonsinfo.domain.usecase.FavouritesUsecase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class FavouriteViewModelTest {

    @RelaxedMockK
    private lateinit var favouritesUsecase: FavouritesUsecase

    private lateinit var favouriteViewModel: FavouriteViewModel

    val entityMock = MutableStateFlow(
        listOf(
            FavouriteCharacterEntity(
                fid = 1,
                name = "Homer Simpson",
                urlImage = "photo.webp"
            ),
            FavouriteCharacterEntity(
                fid = 2,
                name = "Marge Simpson",
                urlImage = "photo-1.webp"
            ),
            FavouriteCharacterEntity(
                fid = 3,
                name = "Bart Simpson",
                urlImage = "photo-2.webp"
            )
        )
    )

    val favouriteCharacterMock = FavouriteCharacterEntity(
        fid = 1,
        name = "Homer Simpson",
        urlImage = "photo.webp"
    )

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
    }

    @Test
    fun getFavourites() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        favouriteViewModel = FavouriteViewModel(favouritesUsecase, dispatcher)

        //When
        coEvery { favouritesUsecase() } returns entityMock

        //Given
        favouriteViewModel
        advanceUntilIdle()

        //Then
        assert(favouriteViewModel.uiState.value == entityMock.value)
    }

    @Test
    fun onSaveFavourite() = runTest {

        val dispatcher = StandardTestDispatcher(testScheduler)
        favouriteViewModel = FavouriteViewModel(favouritesUsecase, dispatcher)

        // Check favourite initial value = false
        assertEquals(false, favouriteViewModel.isFavourite.value)

        // When
        coEvery { favouritesUsecase.saveFavourite(favouriteCharacterMock) } returns favouriteCharacterMock.fid.toLong()

        // Given
        favouriteViewModel.onSaveFavourite(favouriteCharacterMock)
        advanceUntilIdle()

        // Then
        assertEquals(true, favouriteViewModel.isFavourite.value)
    }


    @Test
    fun existsItem() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        favouriteViewModel = FavouriteViewModel(favouritesUsecase, dispatcher)

        // Check favourite initial value
        assertEquals(false, favouriteViewModel.isFavourite.value)

        // When
        coEvery { favouritesUsecase.existsItem(1) } returns true

        // Given
        favouriteViewModel.existsItem(1)
        advanceUntilIdle()

        // Then
        assertEquals(true, favouriteViewModel.isFavourite.value)
    }

    @Test
    fun onDeleteFavourite() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        favouriteViewModel = FavouriteViewModel(favouritesUsecase, dispatcher)

        // Check favourite initial value
        assertEquals(false,favouriteViewModel.isFavourite.value)
        // Save character to favourite
        coEvery { favouritesUsecase.saveFavourite(favouriteCharacterMock) } returns favouriteCharacterMock.fid.toLong()
        favouriteViewModel.onSaveFavourite(favouriteCharacterMock)
        advanceUntilIdle()
        //Check favourite value
        assertEquals(true, favouriteViewModel.isFavourite.value)

        // When
        coEvery { favouritesUsecase.deleteFavourite(favouriteCharacterMock) } returns favouriteCharacterMock.fid

        // Given
        favouriteViewModel.onDeleteFavourite(favouriteCharacterMock)
        advanceUntilIdle()

        // Then
        assertEquals(false, favouriteViewModel.isFavourite.value)
    }

    @Test
    fun `on delete favourite when character is not favourite`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        favouriteViewModel = FavouriteViewModel(favouritesUsecase, dispatcher)

        // Check favourite initial value
        assertEquals(false,favouriteViewModel.isFavourite.value)
        // Save character to favourite
        coEvery { favouritesUsecase.saveFavourite(favouriteCharacterMock) } returns favouriteCharacterMock.fid.toLong()
        favouriteViewModel.onSaveFavourite(favouriteCharacterMock)
        advanceUntilIdle()
        //Check favourite value
        assertEquals(true, favouriteViewModel.isFavourite.value)

        // When
        coEvery { favouritesUsecase.deleteFavourite(entityMock.value.last()) } returns favouriteCharacterMock.fid


        // Given
        favouriteViewModel.onDeleteFavourite(entityMock.value.last())
        advanceUntilIdle()

        // Then
        assertEquals(true, favouriteViewModel.isFavourite.value)
    }


}