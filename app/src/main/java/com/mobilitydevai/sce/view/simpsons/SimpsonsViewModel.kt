/**
 * Created by chrystian.lozada.local (8/10/25)
 */
package com.mobilitydevai.sce.view.simpsons

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobilitydevai.sce.analytics.AnalyticsManager
import com.mobilitydevai.sce.analytics.CrashlyticsManager
import com.mobilitydevai.sce.domain.entity.CharacterEntity
import com.mobilitydevai.sce.domain.entity.SimpsonsEntity
import com.mobilitydevai.sce.domain.usecase.SimpsonsUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SimpsonsViewModel @Inject constructor(
    private val simpsonsUsecase: SimpsonsUsecase,
    private val analyticsManager: AnalyticsManager,
    private val crashlyticsManager: CrashlyticsManager,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    init {
        onGetCharacters()
        // Log screen view
        analyticsManager.logScreenView("SimpsonsScreen", "SimpsonsScreen")
        crashlyticsManager.recordBreadcrumb("SimpsonsScreen", "screen_opened")
    }

    private val _uiState = MutableStateFlow(SimpsonsUiState())

    val uiState: StateFlow<SimpsonsUiState> = _uiState

    private val _indexSelected = MutableStateFlow(0)

    val indexSelected: StateFlow<Int> = _indexSelected

    private val _characterSelected = MutableStateFlow(CharacterEntity())

    val characterSelected: StateFlow<CharacterEntity> = _characterSelected

    var isLoading = MutableStateFlow(false)

    /** Loading the next page at the end of the carousel (the list stays visible). */
    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore

    private val _page = MutableStateFlow("1")

    val page: StateFlow<String> = _page

    private val _isMuted = MutableStateFlow(false)

    val isMuted: StateFlow<Boolean> = _isMuted

    fun onGetCharacters() {
        viewModelScope.launch(dispatcher) {
            delay(100)
            toggleLoading()
            delay(500)
            val response = simpsonsUsecase(page = _page.value)
            _uiState.update { state ->
                response?.let { entity ->
                    state.copy(entity = entity)
                } ?: state
            }
            toggleLoading()
        }
    }

    /**
     * Appends the next page to the current list (continuous carousel).
     * Does nothing if a page is already loading or there are no more pages.
     */
    fun loadNextPage() {
        if (_isLoadingMore.value || isLoading.value) return
        if (_uiState.value.entity.next.isEmpty()) return
        val nextPage = (_page.value.toIntOrNull() ?: return) + 1

        _isLoadingMore.value = true
        viewModelScope.launch(dispatcher) {
            try {
                val response = simpsonsUsecase(page = nextPage.toString())
                if (response != null) {
                    _page.value = nextPage.toString()
                    _uiState.update { state ->
                        state.copy(entity = response.copy(results = state.entity.results + response.results))
                    }
                    analyticsManager.logPageView(nextPage, "characters")
                }
            } finally {
                _isLoadingMore.value = false
            }
        }
    }

    fun toggleLoading() {
        isLoading.value = !isLoading.value
    }

    fun setIndexSelected(index: Int) {
        _indexSelected.value = index
    }

    fun setCharacterSelected(character: CharacterEntity) {
        _characterSelected.value = character
        // Log character selection
        analyticsManager.logCharacterView(character.name, character.id.toString())
        crashlyticsManager.recordBreadcrumb("SimpsonsScreen", "character_selected:${character.name}")
    }

    fun incrementPage() {
        var pageNumber = _page.value.toIntOrNull()
        if (pageNumber != null) {
            pageNumber++
            _page.value = pageNumber.toString()
            // Log page navigation
            analyticsManager.logPageView(pageNumber, "characters")
        }
    }

    fun decrementPage() {
        var pageNumber = _page.value.toIntOrNull()
        if (pageNumber != null) {
            pageNumber--
            _page.value = pageNumber.toString()
        }
    }

    fun speakItemCharacters(index: Int, tts: MutableState<TextToSpeech?>) {
        if (isLoading.value) {
            if (tts.value?.isSpeaking == true) {
                tts.value?.stop()
            }
        } else {
            var text = "There are no phrases"
            if (uiState.value.entity.results[index].phrases.isNotEmpty()) {
                text = uiState.value.entity.results[index].phrases[0]
            }
            val parameters = Bundle().apply {
                putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, if (isMuted.value) 0.0f else 1.0f)
            }
            tts.value?.speak(
                text,
                TextToSpeech.QUEUE_FLUSH,
                parameters,
                "tts_simpsons"
            )
        }
    }

    fun speakItemDetail(tts: MutableState<TextToSpeech?>, text: String) {
        stopSpeak(tts)
        tts.value?.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            ""
        )
    }

    fun stopSpeak(tts: MutableState<TextToSpeech?>) {
        if (tts.value?.isSpeaking == true) {
            tts.value?.stop()
        }
    }

    fun toggleMute(tts: MutableState<TextToSpeech?>) {
        if (tts.value?.isSpeaking == true) {
            tts.value?.stop()
        }
        _isMuted.value = !_isMuted.value
        // Log audio toggle
        analyticsManager.logAudioToggle(_isMuted.value)
        crashlyticsManager.recordBreadcrumb("SimpsonsScreen", "audio_toggled:${_isMuted.value}")
    }
}

data class SimpsonsUiState(
    val entity: SimpsonsEntity = SimpsonsEntity(
        0,
        "",
        "",
        0,
        results = emptyList()
    )
)