package com.mobilitydevai.simpsonsinfo.view.core.mediaplayer.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobilitydevai.simpsonsinfo.view.core.mediaplayer.MainPlayerUiActions
import com.mobilitydevai.simpsonsinfo.view.core.mediaplayer.MainPlayerViewModel

@ExperimentalMaterial3Api
@Composable
fun VoiceScreen(
    uri: String,
    navigateBack: () -> Unit
) {
    val viewModel: MainPlayerViewModel = hiltViewModel()
    val mediaPlayerState by viewModel.playerState.collectAsStateWithLifecycle()
    val sliderPositions by viewModel.sliderPosition.collectAsStateWithLifecycle()

//    DisposableEffect(Unit) {
//        viewModel.initializeMedia(uri)
//        onDispose { viewModel.clearMediaPlayer() }
//    }
    Scaffold(
        bottomBar = {
            MediaPlayerController(
                modifier = Modifier.fillMaxWidth().padding(bottom = 40.dp),
                uri = uri,
                currentTime = sliderPositions,
                totalTime = mediaPlayerState.totalDuration,
                valueRange = 0f..mediaPlayerState.totalDuration.toFloat(),
                mediaPlayerState = mediaPlayerState.current,
                onValueChange = { viewModel.onAction(MainPlayerUiActions.SeekTo(it.toLong())) },
                onAction = viewModel::onAction,
            )
        }
    ) { paddingValues ->
        Text("This is content")
    }
}
