package com.example.simpsonsinfo.domain.usecase

import com.example.simpsonsinfo.data.repository.local.LocalRepository
import com.example.simpsonsinfo.utils.mock.FavouriteMocks.aFavouriteCharacterEmptyList
import com.example.simpsonsinfo.utils.mock.FavouriteMocks.aFavouriteCharacterList
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class FavouritesUsecaseTest {

    @RelaxedMockK
    private lateinit var repository: LocalRepository

    lateinit var favouritesUsecase: FavouritesUsecase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        favouritesUsecase = FavouritesUsecase(repository)
    }

    @Test
    fun getFavouritesEmpty() =
        runBlocking {

            // GIVEN
            coEvery { repository.getAllFavourites() } returns aFavouriteCharacterEmptyList

            // WHEN
            val response = favouritesUsecase()

            // THEN
            coVerify(exactly = 1) { repository.getAllFavourites() }
            assert(response == aFavouriteCharacterEmptyList)
        }

    @Test
    fun getFavourites() =
        runBlocking {

            // GIVEN
            coEvery { repository.getAllFavourites() } returns aFavouriteCharacterList

            // WHEN
            val response = favouritesUsecase()

            // THEN
            coVerify(exactly = 1) { repository.getAllFavourites() }
            assert(response == aFavouriteCharacterList)
        }
}

