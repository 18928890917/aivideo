package com.stylehub.aivideo.ui.settings.feedback

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MarkUnreadChatAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stylehub.aivideo.R

/**
 *
 * Create by league at 2025/11/2
 *
 * Write some description here
 */

@Composable
fun FeedbackSubmitTab(
    issueTypes: List<String>? = null,
    onSubmitClick: (content: String) -> Unit
) {

    var feedbackDescription by remember { mutableStateOf("") }

    Column {

        if (!issueTypes.isNullOrEmpty()) {
            //类型
            Box {
                Column {
                    Row {
                        Text(
                            text = "Issue Type",
                            color = Color.White
                        )
                    }
                }
            }
        }

        //内容
        Box(
            modifier = Modifier.padding(15.dp)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.MarkUnreadChatAlt,
                        contentDescription = "content",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.padding(5.dp))

                    Text(
                        text = "Issue Description",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.padding(10.dp))

                val tfBgColor = Color(0xFF393939)
                TextField(
                    value = feedbackDescription,
                    onValueChange = { feedbackDescription = it },
                    placeholder = {
                        Text(
                            text = "When reporting AI generation issues, please provide the Task ID to expedite troubleshooting and resolution",
                            color = Color.White.copy(0.7f),
                            fontSize = 14.sp
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = tfBgColor,
                        unfocusedContainerColor = tfBgColor,
                        disabledContainerColor = tfBgColor,
                        // 可选：设置光标、边框、文本颜色等
                        focusedIndicatorColor = Color.Transparent,   // 隐藏底部线
                        unfocusedIndicatorColor = Color.Transparent, // 隐藏底部线
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .border(1.dp, Color.White, RoundedCornerShape(8.dp))
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { onSubmitClick(feedbackDescription) },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            modifier = Modifier
                .fillMaxWidth()
            ,
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF9D9FE7), Color(0xFF9DE7A6))
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {

                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painter = painterResource(R.mipmap.ic_submit),
                    contentDescription = "submit",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "Submit",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 15.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }


    }

}