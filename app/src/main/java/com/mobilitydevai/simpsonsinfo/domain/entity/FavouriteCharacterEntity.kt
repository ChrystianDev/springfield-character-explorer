/**
 * Created by chrystian.lozada.local (13/10/25)
 */
package com.mobilitydevai.simpsonsinfo.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

@Entity(tableName = "favourites")
data class FavouriteCharacterEntity(
    @PrimaryKey val fid: Int,
    @ColumnInfo(name = "age") val age: Int?,
    @ColumnInfo(name = "birthdate") val birthdate: String?,
    @ColumnInfo(name = "gender") val gender: String?,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "occupation") val occupation: String?,
    @ColumnInfo(name = "urlImage") val urlImage: String,
    @ColumnInfo(name = "phrases") val phrases: List<String> = emptyList(),
    @ColumnInfo(name = "status") val status: String,
) {
    fun toCharacterEntity(): CharacterEntity {
        return CharacterEntity(
            id = fid,
            age = age,
            birthdate = birthdate,
            gender = gender ?: "-",
            name = name,
            occupation = occupation,
            portrait_path = urlImage,
            phrases = phrases,
            status = status,
        )
    }
}

class ListStringConverter {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return Json.decodeFromString<List<String>>(value)
    }
}