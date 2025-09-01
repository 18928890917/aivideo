package com.stylehub.aivideo.ui.common

import android.widget.VideoView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun VideoPlayer(
    url: String,
    modifier: Modifier = Modifier
) {
    var isMuted by remember { mutableStateOf(true) }
    var videoViewRef by remember { mutableStateOf<VideoView?>(null) }
    Box(modifier = modifier) {
        AndroidView(
            factory = { context ->
                VideoView(context).apply {
                    setVideoPath(url)
                    setOnPreparedListener { mp ->
                        mp.isLooping = true
                        if (isMuted) {
                            mp.setVolume(0f, 0f)
                        } else {
                            mp.setVolume(1f, 1f)
                        }
                        start()
                    }
                    videoViewRef = this
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { videoView ->
                videoViewRef = videoView
                if (!videoView.isPlaying) {
                    videoView.start()
                }
            }
        )
        LaunchedEffect(isMuted, videoViewRef) {
            videoViewRef?.let { vv ->
                try {
                    val mpField = VideoView::class.java.getDeclaredField("mMediaPlayer")
                    mpField.isAccessible = true
                    val mp = mpField.get(vv) as? android.media.MediaPlayer
                    mp?.let {
                        if (isMuted) {
                            it.setVolume(0f, 0f)
                        } else {
                            it.setVolume(1f, 1f)
                        }
                    }
                } catch (_: Exception) {}
            }
        }
        IconButton(
            onClick = { isMuted = !isMuted },
            modifier = Modifier
                .size(40.dp)
                .padding(12.dp)
                .align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = if (isMuted) Icons.Filled.VolumeOff else Icons.Filled.VolumeUp,
                contentDescription = if (isMuted) "Unmute" else "Mute",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
} 