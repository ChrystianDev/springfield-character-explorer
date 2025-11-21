/**
 * Created by chrystian.lozada.local (13/10/25)
 */
package com.mobilitydevai.sce.view.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobilitydevai.sce.domain.entity.LocationsSimpsonsEntity
import com.mobilitydevai.sce.domain.repository.UserRepository
import com.mobilitydevai.sce.domain.usecase.LocationsUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationsViewModel @Inject constructor(
    private val locationsUsecase: LocationsUsecase,
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher,
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

    val page = _page.asStateFlow()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState = _errorState.asStateFlow()

    private fun updateUiStateWithResponse(response: LocationsSimpsonsEntity?) {
        _uiState.update { state ->
            response?.let {
                val newList = state.entity.results + it.results
                state.copy(entity = it.copy(results = newList))
            } ?: state
        }
    }

    private fun generateLikesList() {
        _likesList.value = _uiState.value.entity.results.mapIndexed { index, _ ->
            LikeUiState(like = 0, index = index)
        }
    }

    fun toggleLoading() {
        isLoading.value = !isLoading.value
    }

    fun onFetchLocations() {
        viewModelScope.launch(dispatcher) {
            //TODO: Eliminar delay
            delay(100)
            toggleLoading()
            val response = locationsUsecase(page = _page.value)
            updateUiStateWithResponse(response)
            generateLikesList()
            toggleLoading()
        }
    }

    fun updateLikes(index: Int, like: Int) {
        // Update local state
        _likesList.update { state ->
            state.mapIndexed { indexList, likeUiState ->
                if (indexList == index) {
                    likeUiState.copy(like = likeUiState.like + like)
                } else {
                    likeUiState
                }
            }
        }

        // Update Firestore
        viewModelScope.launch(dispatcher) {
            val uid = userRepository.getCurrentUserUid()
            if (uid != null) {
                val result = if (like > 0) {
                    // User liked
                    userRepository.updateLikes(uid, increment = true)
                } else {
                    // User unliked
                    userRepository.updateUnlikes(uid, increment = true)
                }

                result.onFailure { exception ->
                    _errorState.value = "Error updating likes: ${exception.message}"
                }
            } else {
                _errorState.value = "User not authenticated"
            }
        }
    }

    fun clearError() {
        _errorState.value = null
    }

    fun incrementPage() {
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