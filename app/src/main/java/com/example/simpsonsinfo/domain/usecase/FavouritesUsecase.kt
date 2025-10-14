/**
 * Created by chrystian.lozada.local (13/10/25)
 */
package com.example.simpsonsinfo.domain.usecase

import com.example.simpsonsinfo.data.repository.local.LocalRepository
import com.example.simpsonsinfo.domain.entity.FavouriteCharacterEntity
import javax.inject.Inject

class FavouritesUsecase @Inject constructor(
    private val localRepository: LocalRepository
) {
    suspend operator fun invoke(): List<FavouriteCharacterEntity> {
        return localRepository.getAllFavourites()
    }

    suspend fun saveFavourite(favouriteCharacter: FavouriteCharacterEntity) {
        return localRepository.insertFavourite(favouriteCharacter = favouriteCharacter)
    }

    suspend fun deleteFavourite(favouriteCharacter: FavouriteCharacterEntity) {
        return localRepository.deleteFavourite(favouriteCharacter = favouriteCharacter)
    }
}