package com.mobilitydevai.simpsonsinfo.data.repository.local

import com.mobilitydevai.simpsonsinfo.domain.entity.FavouriteCharacterEntity
import javax.inject.Inject

class LocalRepository @Inject constructor(
    private val favouriteCharacterDao: FavouriteCharacterDao
) {
    fun getAllFavourites() = favouriteCharacterDao.getAll()

    suspend fun existsItem(fid: Int) = favouriteCharacterDao.existsItem(fid)

    fun insertFavourite(favouriteCharacter: FavouriteCharacterEntity) =
         favouriteCharacterDao.insertAll(favouriteCharacter)

    fun deleteFavourite(favouriteCharacter: FavouriteCharacterEntity) =
        favouriteCharacterDao.delete(favouriteCharacter)
}
