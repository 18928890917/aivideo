package com.stylehub.aivideo.ui.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import com.stylehub.aivideo.utils.ScreenUtil
import com.stylehub.aivideo.utils.ScreenUtil.px2dp

/**
 *
 * Create by league at 2025/7/3
 *
 * 自定义底部弹窗
 */
@Composable
fun CommonBottomDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    content: @Composable (onDismissClick: ()-> Unit) -> Unit
) {
    val duration = 300
    val visible = remember { mutableStateOf(false) }

    LaunchedEffect(show) {
        if (show) {
            visible.value = true
        }
    }
    LaunchedEffect(visible.value) {
        if (!visible.value) {
            delay(duration.toLong())
            onDismiss()
        }
    }

    if (!show) return

    Dialog(
        onDismissRequest = { visible.value = false },
        properties = DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = true,
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height((ScreenUtil.getScreenHeight() - ScreenUtil.getTopSafeInset()).px2dp().dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    if (dismissOnClickOutside) {
                        visible.value = false
                    }
                },
            contentAlignment = Alignment.BottomCenter
        ) {
            AnimatedVisibility(
                visible = visible.value,
                enter = fadeIn(tween(duration)) + slideInVertically(
                    tween(duration),
                    initialOffsetY = { it }),
                exit = fadeOut(tween(duration)) + slideOutVertically(
                    tween(duration),
                    targetOffsetY = { it })
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {},
                ) {
                    content {
                        visible.value = false
                    }
                }
            }
        }
    }


}