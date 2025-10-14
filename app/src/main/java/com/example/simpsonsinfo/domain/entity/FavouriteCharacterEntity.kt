/**
 * Created by chrystian.lozada.local (13/10/25)
 */
package com.example.simpsonsinfo.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites")
data class FavouriteCharacterEntity(
    @PrimaryKey val fid: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "urlImage") val urlImage: String?
)