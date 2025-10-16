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
import kotlinx.coroutines.flow.asStateFlow
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
    val uiState: StateFlow<LocationsUiState> = _uiState

    var isLoading = MutableStateFlow(false)

    private var _likesList = MutableStateFlow<List<LikeUiState>>(emptyList())

    val likesList = _likesList.asStateFlow()

    private val _page = MutableStateFlow("1")

    fun onFetchLocations() {
        viewModelScope.launch(Dispatchers.IO) {
            loadingData()
            delay(1000)
            val response = locationsUsecase(page = _page.value)
            _uiState.update { state ->
                if (response != null) {
                    val newList = state.entity.results + response.results
                    state.copy(entity = response.copy(results = newList))
                } else {
                    state
                }
            }
            _uiState.value.entity.results.forEachIndexed { ind, item ->
                _likesList.value = _likesList.value + LikeUiState(like = 0, index = ind)
            }
            dataLoaded()
        }
    }

    fun updateLikes(index: Int, like: Int) {
        _likesList.update { state ->
            state.mapIndexed { indexList, likeUiState ->
                if (indexList == index) {
                    likeUiState.copy(like = likeUiState.like + like)
                } else {
                    likeUiState
                }
            }
        }
    }

    fun loadingData() {
        isLoading.value = true
    }

    fun dataLoaded() {
        isLoading.value = false
    }

    fun downloadMore() {
        var pageNumber = _page.value.toIntOrNull()
        if (pageNumber != null) {
            pageNumber++
            _page.value = pageNumber.toString()
        }
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

data class LikeUiState(
    val like: Int = 0,
    val index: Int = 0
)