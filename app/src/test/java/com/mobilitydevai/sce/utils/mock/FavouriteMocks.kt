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
        name: String = "Homer Simpson",
        urlImage: String = "photo.webp"
    ) = FavouriteCharacterEntity(fid, name, urlImage)
}