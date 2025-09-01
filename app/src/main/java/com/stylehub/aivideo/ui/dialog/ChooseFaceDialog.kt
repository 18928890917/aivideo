package com.stylehub.aivideo.ui.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells.Fixed
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.stylehub.aivideo.R
import com.stylehub.aivideo.network.model.out.RecommendHeadListRespDataModel
import com.stylehub.aivideo.ui.common.topRoundedBorder

@Composable
fun ChooseFaceDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onAddFromAlbum: () -> Unit = {},
    faceList: List<RecommendHeadListRespDataModel.HeadInfo> = emptyList(), // 头像资源id列表，实际项目可替换为图片url等
    onRecommendHeadSelect: (headInfo: RecommendHeadListRespDataModel.HeadInfo) -> Unit = {},
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
                .topRoundedBorder(Color(0x40FFFFFF), cornerRadius = 16.dp)
        ) {
            // 顶部标题和关闭按钮
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, start = 15.dp, end = 15.dp, bottom = 0.dp)
            ) {
                Text(
                    text = "Choose face",
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
            // Add from album
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF232323))
                            .clickable {
                                onAddFromAlbum()
                                it.invoke()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = "Add from album",
                            modifier = Modifier.size(67.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "Add from album",
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(47.dp))

            if (faceList.isNotEmpty()) {
                // 头像网格
                LazyVerticalGrid(
                    columns = Fixed(6),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 250.dp)
                        .padding(horizontal = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(faceList) { headInfo ->
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    onRecommendHeadSelect(headInfo)
                                    it.invoke()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = headInfo.url,
                                contentDescription = "Face",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(10.dp))
                            )
                        }
                    }
                    items(6) {
                        Spacer(Modifier.size(50.dp))
                    }
                }
            }
        }

    }

} 