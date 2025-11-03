package com.mobilitydevai.simpsonsinfo.view.core.mediaplayer

import androidx.media3.common.Player

data class PlayerState(
    val current: MediaPlayerState = MediaPlayerState.IDLE,
    val totalDuration: Long = 0L,
    val currentDuration: Long = 0L
)

enum class MediaPlayerState {
    IDLE,
    PLAYING,
    PAUSED,
    STOPPED,
}


fun Int.toPlayerState(isPLaying: Boolean): MediaPlayerState = when (this) {
    Player.STATE_IDLE -> MediaPlayerState.IDLE
    Player.STATE_ENDED -> MediaPlayerState.STOPPED
    else -> if (isPLaying) MediaPlayerState.PLAYING else MediaPlayerState.PAUSED
}
