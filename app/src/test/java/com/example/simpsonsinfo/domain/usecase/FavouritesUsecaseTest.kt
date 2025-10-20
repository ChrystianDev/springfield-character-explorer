package com.example.simpsonsinfo.domain.usecase

import com.example.simpsonsinfo.data.repository.local.LocalRepository
import com.example.simpsonsinfo.domain.entity.FavouriteCharacterEntity
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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
            val list: Flow<List<FavouriteCharacterEntity>> = MutableStateFlow(listOf())

            // GIVEN
            coEvery { repository.getAllFavourites() } returns list

            // WHEN
            val response = favouritesUsecase()

            // THEN
            coVerify(exactly = 1) { repository.getAllFavourites() }
            assert(response == list)
        }

    @Test
    fun getFavourites() =
        runBlocking {
            val list: Flow<List<FavouriteCharacterEntity>> = MutableStateFlow(listOf(
                FavouriteCharacterEntity(0, "Bart", "bart.png"),
                FavouriteCharacterEntity(1, "Homer", "homer.png"),
                FavouriteCharacterEntity(3, "Lisa", "lisa.png"),
            ))

            // GIVEN
            coEvery { repository.getAllFavourites() } returns list

            // WHEN
            val response = favouritesUsecase()

            // THEN
            coVerify(exactly = 1) { repository.getAllFavourites() }
            assert(response == list)
        }
}

