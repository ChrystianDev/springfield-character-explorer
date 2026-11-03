/**
 * Created by chrystian.lozada.local (23/10/25)
 */
package com.mobilitydevai.sce.utils.mock

import com.mobilitydevai.sce.domain.entity.FavouriteCharacterEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

object FavouriteMocks {
    val aFavouriteCharacterList = MutableStateFlow(
        value = listOf(
            aFavouriteEntityMock(),
            aFavouriteEntityMock(
                fid = 2,
                name = "Marge Simpson",
                urlImage = "photo-1.webp"
            ),
            aFavouriteEntityMock(
                fid = 3,
                name = "Bart Simpson",
                urlImage = "photo-2.webp"
            ),
        )
    )

    val aFavouriteCharacterEmptyList: Flow<List<FavouriteCharacterEntity>> = MutableStateFlow(
        emptyList()
    )

    fun aFavouriteEntityMock(
        fid: Int = 1,
        age: Int = 40,
        birthdate: String = "1977-04-01",
        gender: String = "male",
        name: String = "Homer Simpson",
        occupation: String = "Nuclear Technician",
        urlImage: String = "photo.webp",
        phrases: List<String> = listOf("ohh Marge"),
        status: String = "alive",

        ) = FavouriteCharacterEntity(
        fid,
        age,
        birthdate,
        gender,
        name,
        occupation,
        urlImage,
        phrases,
        status
    )
}