/**
 * Created by chrystian.lozada.local (13/10/25)
 */
package com.mobilitydevai.sce.view.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobilitydevai.sce.analytics.AnalyticsManager
import com.mobilitydevai.sce.analytics.CrashlyticsManager
import com.mobilitydevai.sce.domain.entity.LocationsSimpsonsEntity
import com.mobilitydevai.sce.domain.repository.LikesRepository
import com.mobilitydevai.sce.domain.repository.UserRepository
import com.mobilitydevai.sce.domain.usecase.LocationsUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

@HiltViewModel
class LocationsViewModel @Inject constructor(
    private val locationsUsecase: LocationsUsecase,
    private val userRepository: UserRepository,
    private val likesRepository: LikesRepository,
    private val analyticsManager: AnalyticsManager,
    private val crashlyticsManager: CrashlyticsManager,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocationsUiState())
    val uiState: StateFlow<LocationsUiState> = _uiState

    var isLoading = MutableStateFlow(false)

    private val _page = MutableStateFlow("1")
    val page = _page.asStateFlow()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState = _errorState.asStateFlow()

    /** Like counters from Firestore (imageId -> count), loaded once and updated locally. */
    private val _likeCounts = MutableStateFlow<Map<String, Long>>(emptyMap())

    /** Image ids liked on this device (DataStore). */
    private val likedIds: StateFlow<Set<String>> = likesRepository.likedIds
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptySet())

    /** Images with a like/unlike request in progress, to ignore repeated taps. */
    private val pendingLikes: MutableSet<String> = ConcurrentHashMap.newKeySet()

    val likesList: StateFlow<List<LikeUiState>> =
        combine(_uiState, _likeCounts, likedIds) { state, counts, liked ->
            state.entity.results.mapIndexed { index, location ->
                val imageId = formatImageId(location.id)
                LikeUiState(
                    likeCount = (counts[imageId] ?: 0L).coerceAtLeast(0L).toInt(),
                    isLiked = imageId in liked,
                    index = index
                )
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        onFetchLocations()
        loadLikeCounts()
        // Log screen view
        analyticsManager.logScreenView("LocationScreen", "LocationScreen")
        crashlyticsManager.recordBreadcrumb("LocationScreen", "screen_opened")
    }

    private fun updateUiStateWithResponse(response: LocationsSimpsonsEntity?) {
        _uiState.update { state ->
            response?.let {
                val newList = state.entity.results + it.results
                state.copy(entity = it.copy(results = newList))
            } ?: state
        }
    }

    /** One Firestore read for all counters (covers every page). */
    private fun loadLikeCounts() {
        viewModelScope.launch(dispatcher) {
            likesRepository.getLikeCounts()
                .onSuccess { counts -> _likeCounts.value = counts }
                .onFailure { exception ->
                    analyticsManager.logError("likes_load", exception.message ?: "Unknown error")
                    crashlyticsManager.recordException(exception)
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
            toggleLoading()
        }
    }

    /**
     * Formats the location ID as imgXXX (e.g., 1 -> img001, 12 -> img012, 123 -> img123)
     */
    private fun formatImageId(id: Int): String {
        return "img${id.toString().padStart(3, '0')}"
    }

    /** Likes the item, or removes the like if the user already liked it. */
    fun toggleLike(index: Int) {
        val location = _uiState.value.entity.results.getOrNull(index) ?: return
        val imageId = formatImageId(location.id)
        if (!pendingLikes.add(imageId)) return

        // Optimistic counter update
        val expectedDelta = if (imageId in likedIds.value) -1L else 1L
        updateCount(imageId, expectedDelta)

        viewModelScope.launch(dispatcher) {
            try {
                // Anonymous session so Firestore rules can require request.auth != null
                userRepository.getOrCreateAnonymousUid().getOrThrow()

                val nowLiked = likesRepository.toggleLike(imageId).getOrThrow()
                val actualDelta = if (nowLiked) 1L else -1L
                if (actualDelta != expectedDelta) updateCount(imageId, actualDelta - expectedDelta)

                val voteType = if (nowLiked) "like" else "unlike"
                analyticsManager.logVote(imageId, voteType)
                crashlyticsManager.recordBreadcrumb("LocationScreen", "vote_success:$imageId:$voteType")
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // Revert optimistic update
                updateCount(imageId, -expectedDelta)
                _errorState.value = "Error updating vote: ${e.message}"
                analyticsManager.logError("voting", e.message ?: "Unknown error")
                crashlyticsManager.logVotingError(
                    imageId = imageId,
                    errorMessage = e.message ?: "Vote update failed",
                    exception = e
                )
            } finally {
                pendingLikes.remove(imageId)
            }
        }
    }

    private fun updateCount(imageId: String, delta: Long) {
        _likeCounts.update { counts -> counts + (imageId to (counts[imageId] ?: 0L) + delta) }
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
    val isLiked: Boolean = false,
    val index: Int = 0
)
