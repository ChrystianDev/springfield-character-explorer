/**
 * Created by chrystian.lozada.local (8/10/25)
 */
package com.mobilitydevai.simpsonsinfo.view.simpsons

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobilitydevai.simpsonsinfo.domain.entity.CharacterEntity
import com.mobilitydevai.simpsonsinfo.domain.entity.SimpsonsEntity
import com.mobilitydevai.simpsonsinfo.domain.usecase.SimpsonsUsecase
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
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    init {
        onGetCharacters()
    }

    private val _uiState = MutableStateFlow(SimpsonsUiState())

    val uiState: StateFlow<SimpsonsUiState> = _uiState

    private val _indexSelected = MutableStateFlow(0)

    val indexSelected: StateFlow<Int> = _indexSelected

    private val _characterSelected = MutableStateFlow(CharacterEntity())

    val characterSelected: StateFlow<CharacterEntity> = _characterSelected

    var isLoading = MutableStateFlow(false)

    private val _page = MutableStateFlow("1")

    val page: StateFlow<String> = _page

    private val _isMuted = MutableStateFlow(false)

    val isMuted: StateFlow<Boolean> = _isMuted

    fun onGetCharacters() {
        viewModelScope.launch(dispatcher) {
            delay(100)
            toggleLoading()
            val response = simpsonsUsecase(page = _page.value)
            _uiState.update { state ->
                response?.let { entity ->
                    state.copy(entity = entity)
                } ?: state
            }
            toggleLoading()
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
    }

    fun incrementPage() {
        var pageNumber = _page.value.toIntOrNull()
        if (pageNumber != null) {
            pageNumber++
            _page.value = pageNumber.toString()
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
        if (tts.value?.isSpeaking == true) {
            tts.value?.stop()
        }
        tts.value?.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            ""
        )
    }

    fun toggleMute(tts: MutableState<TextToSpeech?>) {
        if (tts.value?.isSpeaking == true) {
            tts.value?.stop()
        }
        _isMuted.value = !_isMuted.value
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