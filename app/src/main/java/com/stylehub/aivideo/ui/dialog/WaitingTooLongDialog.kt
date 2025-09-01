package com.stylehub.aivideo.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stylehub.aivideo.ui.common.topRoundedBorder

/**
 *
 * Create by league at 2025/7/8
 *
 * Write some description here
 */

@Composable
fun WaitingTooLongDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onRunInTheBackgroundClick: () -> Unit = {}
) {

    CommonBottomDialog(
        show = show,
        onDismiss = onDismiss
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    Color(0xFF111212),
                    RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                )
                .topRoundedBorder(Color(0x40FFFFFF), cornerRadius = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 关闭按钮
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, start = 15.dp, end = 15.dp, bottom = 0.dp)
            ) {
                IconButton(
                    onClick = { it.invoke() },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(45.dp))

            Text(
                text = "Waiting too long?",
                fontSize = 15.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(45.dp))

            Button(
                onClick = {
                    it.invoke()
                    onRunInTheBackgroundClick()
                },
                modifier = Modifier
                    .height(50.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = arrayListOf(Color(0xFF9D9FE7), Color(0xFF9DE7A6))
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clip(RoundedCornerShape(10.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),

            ) {
                Text(
                    text = "Run in the background",
                    fontSize = 13.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(45.dp))
        }

    }

}