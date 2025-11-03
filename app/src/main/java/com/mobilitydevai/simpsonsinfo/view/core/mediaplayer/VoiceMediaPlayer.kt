package com.mobilitydevai.simpsonsinfo.view.core.mediaplayer

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.util.EventLogger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class VoiceMediaPlayer(
    private val context: Context
) {
    private var mediaPlayer: ExoPlayer? = null

    private var listener: Player.Listener? = null

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState = _playerState.asStateFlow()

    fun release() {
        mediaPlayer?.apply {
            listener?.let { removeListener(it) }
        }
        this.release()
        mediaPlayer = null
        listener
    }

    //TODO get() ???
    val currentPosition: Long
        get() = mediaPlayer?.currentPosition?.coerceAtLeast(0) ?: 0


    fun setup(
        vararg url: String,
        playWhenReady: Boolean = true,
        speed: Float = 1f,
    ) {
        initialize(
            playWhenReady = playWhenReady,
            speed = speed
        ) {
            val mediaItem = url.map { it ->
                val url = it
                MediaItem.fromUri(url) }
            addMediaItems(mediaItem)
        }
    }

    fun initialize(
        playWhenReady: Boolean = true,
        speed: Float = 1f,
        setup: ExoPlayer.() -> Unit
    ) {
        // TODO: listener = object : T {} ???
        listener = object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                super.onEvents(player, events)
                // TODO: with ???
                with(player) {
                    _playerState.update { playerState ->
                        playerState.copy(
                            current = playbackState.toPlayerState(isPlaying),
                            totalDuration = duration.coerceAtLeast(0L)
                        )
                    }
                }
            }
        }

        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.stop()
        }

        mediaPlayer = ExoPlayer.Builder(context)
            .build()
            .apply {
                // TODO: let ???
                listener?.let { addListener(it) }
                addAnalyticsListener(EventLogger())
                setup()
                setPlaybackSpeed(speed)
                prepare()
                setPlayWhenReady(playWhenReady)
            }
    }

    /**
     *  Controles del reproductor
     */

    fun resume() {
        mediaPlayer?.play()
    }

    fun stop() {
        mediaPlayer?.stop()
    }

    fun pause() {
        mediaPlayer?.pause()
    }

    fun seekTo(position: Long) {
        mediaPlayer?.seekTo(position)
    }

    fun setPlaybackSpeed(speed: Float) {
        mediaPlayer?.setPlaybackSpeed(speed)
    }
}