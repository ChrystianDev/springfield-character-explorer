package com.example.simpsonsinfo.data.repository.local

import com.example.simpsonsinfo.domain.entity.FavouriteCharacterEntity
import javax.inject.Inject

class LocalRepository @Inject constructor(
    private val favouriteCharacterDao: FavouriteCharacterDao
) {
    suspend fun getAllFavourites() = favouriteCharacterDao.getAll()

    suspend fun existsItem(fid: Int) = favouriteCharacterDao.existsItem(fid)

    suspend fun existItemV2(fid:Int) : Boolean {
        return favouriteCharacterDao.existsItem(fid)
    }

    fun insertFavourite(favouriteCharacter: FavouriteCharacterEntity) =
        favouriteCharacterDao.insertAll(favouriteCharacter)

    fun deleteFavourite(favouriteCharacter: FavouriteCharacterEntity) =
        favouriteCharacterDao.delete(favouriteCharacter)

}
