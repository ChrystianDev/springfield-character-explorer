/**
 * Created by chrystian.lozada.local (13/10/25)
 */
package com.example.simpsonsinfo.domain.usecase

import com.example.simpsonsinfo.data.repository.local.LocalRepository
import com.example.simpsonsinfo.domain.entity.FavouriteCharacterEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavouritesUsecase @Inject constructor(
    private val localRepository: LocalRepository
) {
    //    operator fun invoke(): Flow<List<FavouriteCharacterEntity>> {
//        return localRepository.getAllFavourites()
//    }
    suspend operator fun invoke() = localRepository.getAllFavourites()


    suspend fun existsItem(fid: Int) = localRepository.existsItem(fid)


    suspend fun saveFavourite(favouriteCharacter: FavouriteCharacterEntity) {
        return localRepository.insertFavourite(favouriteCharacter = favouriteCharacter)
    }

    suspend fun deleteFavourite(favouriteCharacter: FavouriteCharacterEntity) {
        return localRepository.deleteFavourite(favouriteCharacter = favouriteCharacter)
    }
}