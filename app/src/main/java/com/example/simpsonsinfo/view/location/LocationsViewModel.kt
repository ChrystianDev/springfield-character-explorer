/**
 * Created by chrystian.lozada.local (13/10/25)
 */
package com.example.simpsonsinfo.view.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpsonsinfo.domain.entity.LocationsSimpsonsEntity
import com.example.simpsonsinfo.domain.usecase.LocationsUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationsViewModel @Inject constructor(
    private val locationsUsecase: LocationsUsecase
) : ViewModel() {

    init {
        onFetchLocations()
    }

    private val _uiState = MutableStateFlow(LocationsUiState())
    private val _isLike = MutableStateFlow(0)
    val uiState: StateFlow<LocationsUiState> = _uiState
    val isLike: StateFlow<Int> = _isLike
    var isLoading = MutableStateFlow(false)

    fun onFetchLocations(){
        viewModelScope.launch(Dispatchers.IO) {
            isLoadingData()
            delay(2000)
            val response = locationsUsecase()
            _uiState.update { state ->
                if (response != null) {
                    state.copy(entity = response)
                } else {
                    state
                }
            }
            dataLoaded()
        }
    }

    fun isLoadingData() {
        isLoading.value = true
    }

    fun dataLoaded() {
        isLoading.value = false
    }

    fun setLike(like: Int) {
        _isLike.value = like
    }
}

data class LocationsUiState(
    val entity: LocationsSimpsonsEntity = LocationsSimpsonsEntity(
        0,
        "",
        "",
        0,
        results = emptyList()
    )
)