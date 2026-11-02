/**
 * Created by chrystian.lozada.local (20/11/25)
 */
package com.mobilitydevai.sce.view.episode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobilitydevai.sce.domain.entity.EpisodesEntity
import com.mobilitydevai.sce.domain.entity.SeasonEntity
import com.mobilitydevai.sce.domain.usecase.EpisodesUsecase
import com.mobilitydevai.sce.domain.usecase.SeasonsUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EpisodesViewModel @Inject constructor(
    private val episodesUsecase: EpisodesUsecase,
    private val seasonsUsecase: SeasonsUsecase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(EpisodesUiState())
    val uiState: StateFlow<EpisodesUiState> = _uiState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage

    private val _selectedEpisodeIndex = MutableStateFlow(0)
    val selectedEpisodeIndex: StateFlow<Int> = _selectedEpisodeIndex

    init {
        fetchEpisodes()
        fetchSeasons()
    }

    fun fetchEpisodes() {
        viewModelScope.launch(dispatcher) {
            delay(100)
            toggleLoading()
            delay(1000)
            val response = episodesUsecase(page = _currentPage.value)
            _uiState.update { state ->
                response?.let { entity ->
                    state.copy(entity = entity)
                } ?: state
            }
            toggleLoading()
        }
    }

    private fun fetchSeasons() {
        viewModelScope.launch(dispatcher) {
            val seasons = seasonsUsecase() ?: return@launch
            _uiState.update { state ->
                state.copy(
                    seasons = seasons,
                    selectedSeason = seasons.firstOrNull()?.number ?: 0,
                    selectedChapter = 1
                )
            }
        }
    }

    fun setSelectedSeason(season: Int) {
        _uiState.update { it.copy(selectedSeason = season, selectedChapter = 1) }
    }

    fun setSelectedChapter(chapter: Int) {
        _uiState.update { it.copy(selectedChapter = chapter) }
    }

    fun loadNextPage() {
        if (_uiState.value.entity.next.isNotEmpty() && !_isLoading.value) {
            _currentPage.value += 1
            fetchEpisodes()
        }
    }

    fun setSelectedEpisodeIndex(index: Int) {
        _selectedEpisodeIndex.value = index
    }

    private fun toggleLoading() {
        _isLoading.value = !_isLoading.value
    }
}

data class EpisodesUiState(
    val entity: EpisodesEntity = EpisodesEntity(),
    val seasons: List<SeasonEntity> = emptyList(),
    val selectedSeason: Int = 0,
    /** 1-based chapter number within [selectedSeason] */
    val selectedChapter: Int = 0
) {
    val selectedSeasonEpisodeCount: Int
        get() = seasons.firstOrNull { it.number == selectedSeason }?.episodeCount ?: 0
}
