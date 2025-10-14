package com.example.simpsonsinfo.data.repository.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.simpsonsinfo.domain.entity.FavouriteCharacterEntity

@Dao
interface FavouriteCharacterDao {
    @Query("SELECT * FROM favourites")
    fun getAll(): List<FavouriteCharacterEntity>

    @Query("SELECT * FROM favourites WHERE fid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<FavouriteCharacterEntity>

    @Insert
    fun insertAll(vararg favouriteCharacters: FavouriteCharacterEntity)

    @Delete
    fun delete(favouriteCharacter: FavouriteCharacterEntity)
}