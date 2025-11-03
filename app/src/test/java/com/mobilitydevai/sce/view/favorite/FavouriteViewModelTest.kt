package com.mobilitydevai.sce.view.favorite

import com.mobilitydevai.sce.domain.usecase.FavouritesUsecase
import com.mobilitydevai.sce.utils.mock.FavouriteMocks.aFavouriteCharacterList
import com.mobilitydevai.sce.utils.mock.FavouriteMocks.aFavouriteEntityMock
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
    }

    @Test
    fun getFavourites() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        favouriteViewModel = FavouriteViewModel(favouritesUsecase, dispatcher)

        //When
        coEvery { favouritesUsecase() } returns aFavouriteCharacterList

        //Given
        favouriteViewModel
        advanceUntilIdle()

        //Then
        coVerify(exactly = 1) { favouritesUsecase() }

        assertEquals(
            true,
            favouriteViewModel.uiState.value == aFavouriteCharacterList.value
        )
    }

    @Test
    fun onSaveFavourite() = runTest {

        val dispatcher = StandardTestDispatcher(testScheduler)
        favouriteViewModel = FavouriteViewModel(favouritesUsecase, dispatcher)

        // Check favourite initial value = false
        assertEquals(false, favouriteViewModel.isFavourite.value)

        // When
        coEvery {
            favouritesUsecase.saveFavourite(
                aFavouriteEntityMock()

            )
        } returns aFavouriteEntityMock().fid.toLong()

        // Given
        favouriteViewModel.onSaveFavourite(aFavouriteEntityMock())
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
        assertEquals(false, favouriteViewModel.isFavourite.value)
        // Save character to favourite
        coEvery { favouritesUsecase.saveFavourite(aFavouriteEntityMock()) } returns aFavouriteEntityMock().fid.toLong()
        favouriteViewModel.onSaveFavourite(aFavouriteEntityMock())
        advanceUntilIdle()
        //Check favourite value
        assertEquals(true, favouriteViewModel.isFavourite.value)

        // When
        coEvery { favouritesUsecase.deleteFavourite(aFavouriteEntityMock()) } returns aFavouriteEntityMock().fid

        // Given
        favouriteViewModel.onDeleteFavourite(aFavouriteEntityMock())
        advanceUntilIdle()

        // Then
        assertEquals(false, favouriteViewModel.isFavourite.value)
    }

    @Test
    fun `on delete favourite when character is not favourite`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        favouriteViewModel = FavouriteViewModel(favouritesUsecase, dispatcher)

        // Check favourite initial value
        assertEquals(false, favouriteViewModel.isFavourite.value)
        // Save character to favourite
        coEvery { favouritesUsecase.saveFavourite(aFavouriteEntityMock()) } returns aFavouriteEntityMock().fid.toLong()
        favouriteViewModel.onSaveFavourite(aFavouriteEntityMock())
        advanceUntilIdle()
        //Check favourite value
        assertEquals(true, favouriteViewModel.isFavourite.value)

        // When
        coEvery { favouritesUsecase.deleteFavourite(aFavouriteCharacterList.value.last()) } returns aFavouriteEntityMock().fid


        // Given
        favouriteViewModel.onDeleteFavourite(aFavouriteCharacterList.value.last())
        advanceUntilIdle()

        // Then
        assertEquals(true, favouriteViewModel.isFavourite.value)
    }
}