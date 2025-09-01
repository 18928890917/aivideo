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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stylehub.aivideo.ui.common.topRoundedBorder

/**
 *
 * Create by league at 2025/7/10
 *
 * Write some description here
 */

@Composable
fun BottomHintMessageDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    hint: String,
    title: String = "",
) {

    CommonBottomDialog(show, onDismiss) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    Color(0xFF111212),
                    RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                )
                .topRoundedBorder(Color(0x22FFFFFF), cornerRadius = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 顶部标题和关闭按钮
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, start = 15.dp, end = 15.dp, bottom = 0.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(top = 24.dp)
                )
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
            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = hint,
                color = Color.White,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(120.dp))

        }

    }
}