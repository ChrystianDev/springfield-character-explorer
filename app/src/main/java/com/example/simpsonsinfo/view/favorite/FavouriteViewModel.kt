/**
 * Created by chrystian.lozada.local (13/10/25)
 */
package com.example.simpsonsinfo.view.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpsonsinfo.domain.entity.FavouriteCharacterEntity
import com.example.simpsonsinfo.domain.usecase.FavouritesUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val favouritesUsecase: FavouritesUsecase
) : ViewModel() {

    init {
        onGetFavourites()
    }

   lateinit var uiState : Flow<List<FavouriteCharacterEntity>>


    private val _isFavourite = MutableStateFlow(false)
    val isFavourite: StateFlow<Boolean> = _isFavourite

    fun onGetFavourites() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = favouritesUsecase()
            uiState = response
        }
    }

    fun existsItem(fid: Int){
        viewModelScope.launch(Dispatchers.IO) {
            val response = favouritesUsecase.existsItem(fid)
            _isFavourite.update {
                response
            }
        }
    }

    fun onSaveFavourite(favouriteCharacter: FavouriteCharacterEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            favouritesUsecase.saveFavourite(favouriteCharacter)
            existsItem(favouriteCharacter.fid)
            onGetFavourites()
        }
    }

    fun onDeleteFavourite(favouriteCharacter: FavouriteCharacterEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            favouritesUsecase.deleteFavourite(favouriteCharacter)
            existsItem(favouriteCharacter.fid)
            onGetFavourites()
        }
    }
}
