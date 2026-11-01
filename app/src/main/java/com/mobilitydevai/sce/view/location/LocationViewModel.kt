/**
 * Created by chrystian.lozada.local (13/10/25)
 */
package com.mobilitydevai.sce.view.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobilitydevai.sce.analytics.AnalyticsManager
import com.mobilitydevai.sce.analytics.CrashlyticsManager
import com.mobilitydevai.sce.config.Constants
import com.mobilitydevai.sce.domain.entity.ImageEntity
import com.mobilitydevai.sce.domain.entity.LocationsSimpsonsEntity
import com.mobilitydevai.sce.domain.repository.ImageRepository
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
    private val imageRepository: ImageRepository,
    private val analyticsManager: AnalyticsManager,
    private val crashlyticsManager: CrashlyticsManager,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    init {
        onFetchLocations()
        // Log screen view
        analyticsManager.logScreenView("LocationScreen", "LocationScreen")
        crashlyticsManager.recordBreadcrumb("LocationScreen", "screen_opened")
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
        _likesList.value = _uiState.value.entity.results.mapIndexed { index, location ->
            LikeUiState(likeCount = 0, index = index)
        }
        // Load existing counts from Firestore
        loadVoteCounts()
    }

    private fun loadVoteCounts() {
        viewModelScope.launch(dispatcher) {
            _uiState.value.entity.results.forEachIndexed { index, location ->
                val imageId = formatImageId(location.id)
                val imageResult = imageRepository.getImage(imageId)
                imageResult.onSuccess { image ->
                    if (image != null) {
                        _likesList.update { state ->
                            state.mapIndexed { listIndex, likeUiState ->
                                if (listIndex == index) {
                                    likeUiState.copy(likeCount = image.likeCount.toInt())
                                } else {
                                    likeUiState
                                }
                            }
                        }
                    }
                }
            }
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

    /**
     * Formats the location ID as imgXXX (e.g., 1 -> img001, 12 -> img012, 123 -> img123)
     */
    private fun formatImageId(id: Int): String {
        return "img${id.toString().padStart(3, '0')}"
    }

    fun like(index: Int) {
        viewModelScope.launch(dispatcher) {
            // Anonymous Firebase uid: one vote per installation
            val uid = userRepository.getOrCreateAnonymousUid().getOrElse { exception ->
                _errorState.value = "Error updating vote: ${exception.message}"
                analyticsManager.logError("anonymous_auth", exception.message ?: "Unknown error")
                crashlyticsManager.recordException(exception)
                return@launch
            }

            // Update local state optimistically
            _likesList.update { state ->
                state.mapIndexed { indexList, likeUiState ->
                    if (indexList == index) {
                        likeUiState.copy(likeCount = likeUiState.likeCount + 1)
                    } else {
                        likeUiState
                    }
                }
            }

            // Update Firestore
            val location = _uiState.value.entity.results.getOrNull(index)
            if (location != null) {
                val imageId = formatImageId(location.id)
                val url = "${Constants.BASE_URL_IMAGE_LARGE}${location.imagePath}"

                // Ensure image document exists
                val existingImage = imageRepository.getImage(imageId).getOrNull()
                if (existingImage == null) {
                    // Create the image document
                    val newImage = ImageEntity(
                        imageId = imageId,
                        url = url,
                        description = location.name,
                        likeCount = 0,
                        timestamp = System.currentTimeMillis().toString()
                    )
                    imageRepository.createOrUpdateImage(newImage)
                }

                // Add like (no-op if this user already liked the image)
                val voteType = "like"
                val result = imageRepository.addLike(imageId, uid)

                result.onSuccess {
                    // Log successful vote
                    analyticsManager.logVote(imageId, voteType)
                    crashlyticsManager.recordBreadcrumb("LocationScreen", "vote_success:$imageId:$voteType")

                    // Reload the actual counts from Firestore
                    val updatedImage = imageRepository.getImage(imageId).getOrNull()
                    if (updatedImage != null) {
                        _likesList.update { state ->
                            state.mapIndexed { listIndex, likeUiState ->
                                if (listIndex == index) {
                                    likeUiState.copy(likeCount = updatedImage.likeCount.toInt())
                                } else {
                                    likeUiState
                                }
                            }
                        }
                    }
                }

                result.onFailure { exception ->
                    _errorState.value = "Error updating vote: ${exception.message}"

                    // Log error
                    analyticsManager.logError("voting", exception.message ?: "Unknown error")
                    crashlyticsManager.logVotingError(
                        imageId = imageId,
                        errorMessage = exception.message ?: "Vote update failed",
                        exception = exception
                    )

                    // Reload counts on failure to revert optimistic update
                    val reloadedImage = imageRepository.getImage(imageId).getOrNull()
                    if (reloadedImage != null) {
                        _likesList.update { state ->
                            state.mapIndexed { listIndex, likeUiState ->
                                if (listIndex == index) {
                                    likeUiState.copy(likeCount = reloadedImage.likeCount.toInt())
                                } else {
                                    likeUiState
                                }
                            }
                        }
                    }
                }
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
    val likeCount: Int = 0,
    val index: Int = 0
)