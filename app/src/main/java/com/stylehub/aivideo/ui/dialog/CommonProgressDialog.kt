package com.stylehub.aivideo.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stylehub.aivideo.R
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun CommonProgressDialog(
    show: Boolean,
    progress: Float, // 0f ~ 1f
    text: String
) {
    if (!show) return

    Dialog(onDismissRequest = { /* 禁止外部点击关闭 */ }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF181A20))
                .padding(vertical = 32.dp, horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = progress,
                        strokeWidth = 8.dp,
                        modifier = Modifier.size(80.dp),
                        color = Color.Transparent, // We'll draw our own gradient below
                        trackColor = Color(0xFF23253A)
                    )
                    // Gradient arc overlay
                    androidx.compose.foundation.Canvas(modifier = Modifier.size(80.dp)) {
                        drawArc(
                            brush = Brush.sweepGradient(
                                listOf(
                                    Color(0xFF7CFB9A),
                                    Color(0xFF9D9FE7),
                                    Color(0xFF7CFB9A)
                                )
                            ),
                            startAngle = -90f,
                            sweepAngle = 360f * progress,
                            useCenter = false,
                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 5.dp.toPx())
                        )
                    }
                    Icon(
                        painter = painterResource(id = R.drawable.ic_photo),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.height(27.dp))
                Text(
                    text = text,
                    color = Color.White,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
} 