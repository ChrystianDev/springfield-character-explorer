package com.example.simpsonsinfo.data.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.simpsonsinfo.domain.entity.FavouriteCharacterEntity
import com.example.simpsonsinfo.domain.entity.ListStringConverter

/**
 * Created by chrystian.lozada.local (13/10/25)
 */
@Database(entities = [FavouriteCharacterEntity::class], version = 1)
@TypeConverters(ListStringConverter::class)
abstract class DataBaseRoom : RoomDatabase() {
    abstract fun favouriteCharacterDao(): FavouriteCharacterDao
}
