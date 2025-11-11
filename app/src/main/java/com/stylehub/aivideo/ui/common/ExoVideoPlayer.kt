package com.stylehub.aivideo.ui.common

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
@Composable
fun ExoVideoPlayer(
    url: String,
    modifier: Modifier = Modifier,
    mutedDefault: Boolean = true,
    repeatMode: Int = ExoPlayer.REPEAT_MODE_ONE,           // 默认循环播放
    resizeMode: Int = AspectRatioFrameLayout.RESIZE_MODE_ZOOM // 默认铺满裁剪
) {

    var isMuted by remember { mutableStateOf(mutedDefault) }
    Box(modifier = modifier) {

        val context = androidx.compose.ui.platform.LocalContext.current

        val exoPlayer = remember(context, repeatMode) {
            ExoPlayer.Builder(context).build().apply {
                setMediaItem(MediaItem.fromUri(url))
                this.repeatMode = repeatMode
                prepare()
                playWhenReady = true
                volume = if (isMuted) 0f else 1f
            }
        }

        // 更新音量（响应 isMuted 变化）
        DisposableEffect(isMuted) {
            exoPlayer.volume = if (isMuted) 0f else 1f
            onDispose { }
        }

        // 更新 repeatMode（如果未来需要动态改变，可加到 remember key 中）
        // 当前 repeatMode 是初始化时设置的，如需运行时切换，需额外逻辑

        // 释放播放器
        DisposableEffect(Unit) {
            onDispose {
                exoPlayer.release()
            }
        }

        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    this.resizeMode = resizeMode
                    useController = false
                    setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                }
            },
            modifier = modifier
        )

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