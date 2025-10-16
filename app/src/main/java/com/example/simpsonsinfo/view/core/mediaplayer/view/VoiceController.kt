/**
 * Created by chrystian.lozada.local (16/10/25)
 */
package com.example.simpsonsinfo.view.core.mediaplayer.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.simpsonsinfo.R
import com.example.simpsonsinfo.view.core.components.SpaceV10
import com.example.simpsonsinfo.view.core.mediaplayer.MainPlayerUiActions
import com.example.simpsonsinfo.view.core.mediaplayer.MediaPlayerState

@ExperimentalMaterial3Api
@Composable
fun MediaPlayerController(
    uri: String,
    currentTime: Long,
    totalTime: Long,
    speed: Float = 1f,
    mediaPlayerState: MediaPlayerState = MediaPlayerState.IDLE,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    modifier: Modifier = Modifier,
    onValueChange: (Float) -> Unit,
    onAction: (MainPlayerUiActions) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Slider(
                value = currentTime.toFloat(),
                onValueChange = onValueChange,
                valueRange = valueRange,
                colors = SliderDefaults.colors(
//                            thumbColor = SliderBrushColor(color = MaterialTheme.colorScheme.onSurface),
//                            activeTrackColor = SliderBrushColor(color = MaterialTheme.colorScheme.onSurface),
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "$currentTime",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "$totalTime",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

        SpaceV10()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = {
//                    openSpeedBottomSheet
                }
            ) {
                Text(
                    text = "${speed}x",
                    fontWeight = FontWeight.Bold
                )
            }
            IconButton(
                onClick = {
                    val seekTo = (currentTime - 10000L).coerceAtLeast(0)
                    onAction(MainPlayerUiActions.SeekTo(seekTo))
                }) {
                Icon(
                    painter = painterResource(R.drawable.icon_thumb_up_unselected),
                    contentDescription = "Replay 10 seconds"
                )
            }
            OutlinedIconButton(
                onClick = {
                    when (mediaPlayerState) {
                        MediaPlayerState.PLAYING, MediaPlayerState.IDLE -> onAction(
                            MainPlayerUiActions.Pause
                        )

                        MediaPlayerState.STOPPED
                            -> onAction(MainPlayerUiActions.Play(uri))

                        MediaPlayerState.PAUSED
                            -> onAction(MainPlayerUiActions.Resume)
                    }
                },
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.onSurface.copy(0.2f)
                )
            ) {
                Icon(
                    painter = if (mediaPlayerState == MediaPlayerState.PLAYING) painterResource(
                        R.drawable.icon_volume_disabled
                    ) else painterResource(
                        R.drawable.icon_play
                    ),
                    contentDescription = "Play"
                )
            }
            IconButton(
                onClick = {
                    val seekTo = (currentTime + 10000L).coerceAtMost(totalTime)
                    onAction(MainPlayerUiActions.SeekTo(seekTo))
                })
            {
                Icon(
                    painter = painterResource(
                        R.drawable.icon_volume_disabled
                    ),
                    contentDescription = "Forward 10 seconds"
                )
            }
            TextButton(
                onClick = {
//                        openSpeedBottomSheet
                }) {
                Text(
                    text = "${speed}x",
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
