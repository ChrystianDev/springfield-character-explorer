/**
 * Created by chrystian.lozada.local (13/10/25)
 */
package com.example.simpsonsinfo.view.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpsonsinfo.domain.entity.FavouriteCharacterEntity
import com.example.simpsonsinfo.domain.usecase.FavouritesUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val favouritesUsecase: FavouritesUsecase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    init {
        onGetFavourites()
    }

    private val _uiState =
        MutableStateFlow<List<FavouriteCharacterEntity>>(emptyList())
    val uiState: StateFlow<List<FavouriteCharacterEntity>> = _uiState

    private val _isFavourite = MutableStateFlow(false)
    val isFavourite: StateFlow<Boolean> = _isFavourite

    fun onGetFavourites() {
        viewModelScope.launch(dispatcher) {
            val response = favouritesUsecase()
            response.collect { list ->
                _uiState.value = list
            }
        }
    }

    fun existsItem(fid: Int) {
        viewModelScope.launch(dispatcher) {
            val response = favouritesUsecase.existsItem(fid)
            _isFavourite.update {
                response
            }
        }
    }

    fun onSaveFavourite(favouriteCharacter: FavouriteCharacterEntity) {
        viewModelScope.launch(dispatcher) {
            val response = favouritesUsecase.saveFavourite(favouriteCharacter)
            if(response.toInt() == favouriteCharacter.fid){
                _isFavourite.value = true
            }
            onGetFavourites()
        }
    }

    fun onDeleteFavourite(favouriteCharacter: FavouriteCharacterEntity) {
        viewModelScope.launch(dispatcher) {
            val response = favouritesUsecase.deleteFavourite(favouriteCharacter)
            if(response == favouriteCharacter.fid){
                _isFavourite.value = false
            }
            onGetFavourites()
        }
    }
}
