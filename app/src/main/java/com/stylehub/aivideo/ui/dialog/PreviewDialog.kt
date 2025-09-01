package com.stylehub.aivideo.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.stylehub.aivideo.constants.MyTaskImgTypeEnum
import com.stylehub.aivideo.utils.ScreenUtil
import com.stylehub.aivideo.utils.ScreenUtil.dp2px
import com.stylehub.aivideo.utils.ScreenUtil.px2dp
import com.stylehub.aivideo.ui.common.VideoPlayer

@Composable
fun PreviewDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onDownloadClick: () -> Unit,
    model: Any? = null,
    mediaType: Int = MyTaskImgTypeEnum.Image.code,
    title: String = "",
) {

    if (!show)
        return

    CommonBottomDialog(show, onDismiss) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height((ScreenUtil.getScreenHeight() - 110.dp2px()).px2dp().dp)
                .padding(start = 15.dp, end = 15.dp, bottom = 40.dp),
        ) {

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .border(1.dp, Color(0x20FFFFFF), RoundedCornerShape(18.dp))
                ,
                colors = CardDefaults.cardColors(Color(0xFF111212)),
                elevation = CardDefaults.elevatedCardElevation(1.dp)

                ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 顶部标题和关闭按钮
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            fontSize = 15.sp,
                            color = Color.White
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

                    Spacer(modifier = Modifier.height(19.dp))

                    // 大图
                    if (mediaType == MyTaskImgTypeEnum.Image.code) {

                        AsyncImage(
                            model = model,
                            contentDescription = "preview",
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(horizontal = 27.dp)
                                .clip(RoundedCornerShape(20.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        VideoPlayer(
                            url = model!!as String,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(horizontal = 27.dp)
                                .clip(RoundedCornerShape(20.dp)),
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    //下载按钮
                    Button(
                        onClick = {
                            it.invoke()
                            onDownloadClick()
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
                            text = "Download",
                            fontSize = 13.sp,
                            color = Color.Black
                        )
                    }

                }

            }

        }


    }
}
