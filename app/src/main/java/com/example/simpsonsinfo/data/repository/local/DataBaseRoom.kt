package com.example.simpsonsinfo.data.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.simpsonsinfo.domain.entity.FavouriteCharacterEntity

/**
 * Created by chrystian.lozada.local (13/10/25)
 */
@Database(entities = [FavouriteCharacterEntity::class], version = 1)
abstract class DataBaseRoom : RoomDatabase() {
    abstract fun favouriteCharacterDao(): FavouriteCharacterDao
}
