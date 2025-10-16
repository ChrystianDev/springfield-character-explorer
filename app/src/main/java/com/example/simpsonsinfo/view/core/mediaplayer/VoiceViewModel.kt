package com.example.simpsonsinfo.view.core.mediaplayer

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

sealed interface MainPlayerUiActions {
    data class Play(val uri: String) : MainPlayerUiActions
    data object Pause : MainPlayerUiActions
    data object Resume : MainPlayerUiActions
    data class SeekTo(val position: Long) : MainPlayerUiActions
    data class SetSpeed(val speed: Float) : MainPlayerUiActions
}

@HiltViewModel
class MainPlayerViewModel @Inject constructor(
    private val mediaPlayer: VoiceMediaPlayer
) :
    ViewModel() {


    val playerState: StateFlow<PlayerState>
        get() = mediaPlayer.playerState
    private val _sliderPosition = MutableStateFlow(0L)
    val sliderPosition = _sliderPosition.asStateFlow()

    init {
        onAction(MainPlayerUiActions.Play("https://s3.amazonaws.com/scifri-episodes/scifri20181123-episode.mp3"))
    }

    fun onAction(action: MainPlayerUiActions) {
        when (action) {
//            is MainPlayerUiActions.Play -> mediaPlayer.setup(action.uri)
            is MainPlayerUiActions.Play -> mediaPlayer.setup(action.uri)
            is MainPlayerUiActions.Resume -> mediaPlayer.resume()
            is MainPlayerUiActions.Pause -> mediaPlayer.pause()
            is MainPlayerUiActions.SeekTo -> mediaPlayer.seekTo(action.position)
            is MainPlayerUiActions.SetSpeed -> mediaPlayer.setPlaybackSpeed(action.speed)
        }
    }
}