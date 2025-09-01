package com.stylehub.aivideo.ui.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stylehub.aivideo.R
import com.stylehub.aivideo.ui.common.topRoundedBorder

@Composable
fun FirstUploadHintDialog(
    show: Boolean,
    onDismiss: () -> Unit
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
                .topRoundedBorder(Color(0x20FFFFFF), cornerRadius = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = { it.invoke() },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "How to make a high-quality creation?",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            // Good Examples
            ExampleGroup(
                title = "Good Examples",
                titleColor = Color(0xFF7CFB9A),
                iconRes = R.drawable.ic_good_examples,
                label = "Claar,frontal faces",
                backgroundRes = R.drawable.bg_good_examples,
                imgIdArr = arrayOf(R.mipmap.img_good_example_1, R.mipmap.img_good_example_2, R.mipmap.img_good_example_3)
            )
            // Bad Examples
            ExampleGroup(
                title = "Bad Examples",
                titleColor = Color(0xFFFF6B6B),
                iconRes = R.drawable.ic_bad_examples,
                label = "Glasses,side faces,masks",
                backgroundRes = R.drawable.bg_bad_examples,
                imgIdArr = arrayOf(R.mipmap.img_bad_example_1, R.mipmap.img_bad_example_2, R.mipmap.img_bad_example_3),
                titleAlignment = Alignment.End
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Note: Do not use face-swap for illegal purposes.",
                color = Color(0xFF888888),
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(25.dp))
            Button(
                onClick = { it.invoke() },
                modifier = Modifier
                    .width(220.dp)
                    .height(50.dp)
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFF9D9FE7), Color(0xFF9DE7A6))
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Got it!",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun ExampleGroup(
    title: String,
    titleColor: Color,
    iconRes: Int,
    label: String,
    backgroundRes: Int,
    imgIdArr: Array<Int>,
    titleAlignment: Alignment.Horizontal = Alignment.Start
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(254.dp)
    ) {
        // 背景图片
        Image(
            painter = painterResource(id = backgroundRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF111212)),
            contentScale = ContentScale.FillBounds
        )

        // 内容
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if (titleAlignment == Alignment.End) Arrangement.End else Arrangement.Start
            ) {
                Icon(
                    painter = painterResource(id = if (titleColor == Color(0xFF7CFB9A)) R.drawable.ic_good_examples else R.drawable.ic_bad_examples),
                    contentDescription = null,
                    tint = titleColor,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    color = titleColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 25.dp, end = 30.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                repeat(3) {
                    Image(
                        painter = painterResource(id = imgIdArr[it]),
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .height(154.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = label,
                color = Color.White,
                fontSize = 15.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(13.dp))
        }
    }
} 