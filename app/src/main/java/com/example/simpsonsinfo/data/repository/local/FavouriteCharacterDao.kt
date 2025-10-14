package com.example.simpsonsinfo.data.repository.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.simpsonsinfo.domain.entity.FavouriteCharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteCharacterDao {
    @Query("SELECT * FROM favourites")
    fun getAll(): Flow<List<FavouriteCharacterEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favourites WHERE fid = :fid)")
    suspend fun existsItem(fid: Int): Boolean

    @Insert
    fun insertAll(vararg favouriteCharacters: FavouriteCharacterEntity)

    @Delete
    fun delete(favouriteCharacter: FavouriteCharacterEntity)
}