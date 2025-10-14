/**
 * Created by chrystian.lozada.local (13/10/25)
 */
package com.example.simpsonsinfo.view.favorite

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpsonsinfo.domain.entity.FavouriteCharacterEntity
import com.example.simpsonsinfo.domain.entity.LocationsSimpsonsEntity
import com.example.simpsonsinfo.domain.usecase.FavouritesUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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

    private val _uiState = MutableStateFlow(FavouritesUiState())
    val uiState: StateFlow<FavouritesUiState> = _uiState

    fun onGetFavourites() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = favouritesUsecase()
            _uiState.update { state ->
                if (response.isNotEmpty()) {
                    state.copy(list = response)
                } else {
                    state.copy(list = emptyList())
                }
            }
        }
    }

    fun onSaveFavourite(favouriteCharacter: FavouriteCharacterEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            favouritesUsecase.saveFavourite(favouriteCharacter)
            onGetFavourites()
        }
    }

    fun onDeleteFavourite(favouriteCharacter: FavouriteCharacterEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            favouritesUsecase.deleteFavourite(favouriteCharacter)
            onGetFavourites()
        }
    }
}

data class FavouritesUiState(
    val list: List<FavouriteCharacterEntity> = emptyList(),
)