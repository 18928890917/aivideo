package com.stylehub.aivideo.ui.common

/**
 *
 * Create by league at 2025/7/4
 *
 * Write some description here
 */
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.topRoundedBorder(
    color: Color,
    borderWidth: Dp = 1.dp,
    cornerRadius: Dp = 10.dp
): Modifier = this.then(
    Modifier.drawWithContent {
        drawContent()
        val strokeWidth = borderWidth.toPx()
        val radius = cornerRadius.toPx()
        val w = size.width
        if (w <= 0f) return@drawWithContent
        val path = androidx.compose.ui.graphics.Path().apply {
            // 从左侧圆角起点
            moveTo(0f, radius)
            arcTo(
                rect = androidx.compose.ui.geometry.Rect(0f, 0f, radius * 2, radius * 2),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            // 顶部直线到右上圆角
            lineTo(w - radius, 0f)
            arcTo(
                rect = androidx.compose.ui.geometry.Rect(w - radius * 2, 0f, w, radius * 2),
                startAngleDegrees = 270f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
        }
        drawPath(path, color, style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth))
    }
)