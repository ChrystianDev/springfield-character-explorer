package com.example.simpsonsinfo.data.repository.local

import com.example.simpsonsinfo.domain.entity.FavouriteCharacterEntity
import javax.inject.Inject

class LocalRepository @Inject constructor(
    private val favouriteCharacterDao: FavouriteCharacterDao
) {
    fun getAllFavourites() = favouriteCharacterDao.getAll()

    fun insertFavourite(favouriteCharacter: FavouriteCharacterEntity) =
        favouriteCharacterDao.insertAll(favouriteCharacter)

    fun deleteFavourite(favouriteCharacter: FavouriteCharacterEntity) =
        favouriteCharacterDao.delete(favouriteCharacter)

}
