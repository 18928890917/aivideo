package com.stylehub.aivideo.ui.home.dance

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stylehub.aivideo.R
import com.stylehub.aivideo.ui.common.CommonEmptyView
import com.stylehub.aivideo.ui.common.PowerAsyncImage
import com.stylehub.aivideo.ui.home.HomeActivity
import com.stylehub.aivideo.utils.AppRouterManager
import kotlinx.coroutines.launch

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun DanceTab(innerPadding: PaddingValues, homeActivity: HomeActivity) {

    val viewModel = homeActivity.mViewModel
    val danceTabData = viewModel.uiStateData.value.danceTabData

    // 当切换到DanceTab时加载数据
    LaunchedEffect(Unit) {
        viewModel.loadImg2VideoPoseTemplate()
    }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val showFab by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0
        }
    }

    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF10131A), Color(0xFF23253A))
                    )
                )
        ) {
            // 显示加载状态
            if (danceTabData.isImg2VideoPoseTemplateLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color.White
                        )
                    }
                }
            }

            // 显示模板
            if (danceTabData.img2VideoPoseTemplates.isNotEmpty()) {
                val rows = danceTabData.img2VideoPoseTemplates.chunked(2)
                items(rows) { rowTemplates ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp)
                    ) {
                        for (template in rowTemplates) {
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(173f / 308f)
                                    .padding(5.dp)
                                    .clickable {
                                        AppRouterManager.enterDancePhotoSwapActivity(
                                            template = template
                                        )
                                    },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Box(Modifier.fillMaxSize()) {
                                    // 显示模板图片
                                    PowerAsyncImage(
                                        model = template.animate ?: "",
                                        contentDescription = template.getTmpName(),
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )

                                    if (template.getCredit() > 0) {
                                        // 显示模板信息
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .align(Alignment.BottomStart)
                                                .background(Color.White.copy(alpha = 0.8f))
                                                .padding(2.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column {
                                                Text(
                                                    text = "${template.getCredit()} credits",
                                                    color = Color.Black,
                                                    fontSize = 10.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (rowTemplates.size < 2) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        //无数据
        if (!danceTabData.isImg2VideoPoseTemplateLoading && danceTabData.img2VideoPoseTemplates.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CommonEmptyView()
            }
        }

        if (showFab) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 24.dp, bottom = 144.dp)
            ) {
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            color = Color(0xCC5A5B5B),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = R.mipmap.ic_top),
                            contentDescription = "Top",
                            modifier = Modifier
                                .size(28.dp)
                                .padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun DanceTabPreview() {
//    DanceTab(innerPadding = PaddingValues(0.dp))
//}