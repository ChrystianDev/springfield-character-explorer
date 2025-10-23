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

    operator fun invoke() = localRepository.getAllFavourites()

    suspend fun existsItem(fid: Int) = localRepository.existsItem(fid)

    fun saveFavourite(favouriteCharacter: FavouriteCharacterEntity) : Long {
        return localRepository.insertFavourite(favouriteCharacter = favouriteCharacter)
    }

    fun deleteFavourite(favouriteCharacter: FavouriteCharacterEntity) =
         localRepository.deleteFavourite(favouriteCharacter = favouriteCharacter)

}