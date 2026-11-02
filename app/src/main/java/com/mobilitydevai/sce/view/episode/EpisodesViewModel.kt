/**
 * Created by chrystian.lozada.local (20/11/25)
 */
package com.mobilitydevai.sce.view.episode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobilitydevai.sce.domain.entity.EpisodeEntity
import com.mobilitydevai.sce.domain.entity.SeasonEntity
import com.mobilitydevai.sce.domain.usecase.SeasonsUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EpisodesViewModel @Inject constructor(
    private val seasonsUsecase: SeasonsUsecase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(EpisodesUiState())
    val uiState: StateFlow<EpisodesUiState> = _uiState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    /** Index in the carousel of the selected season; chapter number = index + 1 */
    private val _selectedEpisodeIndex = MutableStateFlow(0)
    val selectedEpisodeIndex: StateFlow<Int> = _selectedEpisodeIndex

    private var seasonJob: Job? = null

    init {
        fetchSeasons()
    }

    fun fetchSeasons() {
        viewModelScope.launch(dispatcher) {
            _isLoading.value = true
            val seasons = seasonsUsecase()
            if (seasons != null) {
                _uiState.update { state ->
                    state.copy(
                        seasons = seasons,
                        selectedSeason = seasons.firstOrNull()?.number ?: 0
                    )
                }
                _selectedEpisodeIndex.value = 0
            }
            _isLoading.value = false
        }
    }

    /**
     * Shows the loading while the carousel is switched to the episodes of [season],
     * starting at its first chapter.
     */
    fun setSelectedSeason(season: Int) {
        if (season == _uiState.value.selectedSeason) return
        seasonJob?.cancel()
        seasonJob = viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isSeasonLoading = true) }
            _uiState.update { it.copy(selectedSeason = season) }
            _selectedEpisodeIndex.value = 0
            // Episodes are already in memory; keep the loading visible long enough to be noticed
            delay(SEASON_LOADING_MILLIS)
            _uiState.update { it.copy(isSeasonLoading = false) }
        }
    }

    fun setSelectedEpisodeIndex(index: Int) {
        _selectedEpisodeIndex.value = index
    }

    private companion object {
        const val SEASON_LOADING_MILLIS = 600L
    }
}

data class EpisodesUiState(
    val seasons: List<SeasonEntity> = emptyList(),
    val selectedSeason: Int = 0,
    val isSeasonLoading: Boolean = false
) {
    /** Episodes shown in the carousel: the ones of [selectedSeason] */
    val episodes: List<EpisodeEntity>
        get() = seasons.firstOrNull { it.number == selectedSeason }?.episodes.orEmpty()
}
