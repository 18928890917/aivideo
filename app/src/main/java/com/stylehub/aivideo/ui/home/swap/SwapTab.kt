package com.stylehub.aivideo.ui.home.swap

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import com.stylehub.aivideo.R
import com.stylehub.aivideo.network.model.out.ClothesTemplateRespDataModel
import com.stylehub.aivideo.network.model.out.FaceSwapVideoTemplateRespDataModel
import com.stylehub.aivideo.network.model.out.GetImageTemplateRespDataModel
import com.stylehub.aivideo.ui.common.CommonEmptyView
import com.stylehub.aivideo.ui.home.HomeActivity
import com.stylehub.aivideo.ui.home.SwapTabsEnum
import com.stylehub.aivideo.utils.AppRouterManager
import com.stylehub.aivideo.utils.LoginManager
import kotlin.math.min

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwapTab(innerPadding: PaddingValues, homeActivity: HomeActivity) {

    val viewModel = homeActivity.mViewModel
    val swapTabData = viewModel.uiStateData.value.swapTabData
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val showFab by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0
        }
    }

    // 当切换到DanceTab时加载数据
    LaunchedEffect(Unit) {
        viewModel.switchTab(swapTabData.currentSelectTab)
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

            stickyHeader {
                Column(
                    Modifier.background(Color(0xFF23253A))
                ) {
                    CustomTabs(
                        selectedTab = swapTabData.currentSelectTab,
                        onTabSelected = { viewModel.switchTab(it) })
                }
            }

            // 显示加载状态
            if (viewModel.isCurrentTabLoading()) {
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

            // 显示无数据信息
            if (viewModel.isCurrentTabNoData()) {
                item {
                    CommonEmptyView()
                }
            } else {

                // 显示模板
                if (viewModel.getCurrentTabData().isNotEmpty()) {

                    val rows = viewModel.getCurrentTabData().chunked(2)
                    items(rows) { rowTemplates ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 5.dp)) {
                            for (template in rowTemplates) {
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(173f / 308f)
                                        .padding(5.dp)
                                        .clickable {
                                            when (swapTabData.currentSelectTab) {
                                                SwapTabsEnum.PHOTO -> AppRouterManager.enterImageFaceSwapActivity(
                                                    template = template as GetImageTemplateRespDataModel
                                                )
                                                SwapTabsEnum.VIDEO -> AppRouterManager.enterVideoFaceSwapActivity(
                                                    template = template as FaceSwapVideoTemplateRespDataModel
                                                )
                                                SwapTabsEnum.CLOTHES -> AppRouterManager.enterClothesSwapActivity(
                                                    template = template as ClothesTemplateRespDataModel
                                                )
                                            }
                                        },
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Box(Modifier.fillMaxSize()) {
                                        // 显示模板图片
                                        AsyncImage(
                                            model = template.getPreviewUrl(),
                                            contentDescription = template.getTmpName(),
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                        )

                                        if (template.getTmpCredits() != null) {
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
                                                    if (template.getTmpCredits() != null) {
                                                        Text(
                                                            text = "${template.getTmpCredits()} credits",
                                                            color = Color.Black,
                                                            fontSize = 10.sp
                                                        )
                                                    }
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


/**
 * 梯形的tab
 */
class TrapezoidShape(
    private val cornerRadius: Float = 24f,
    private val bottomOverlap: Float = 0f // px
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val topWidth = size.width
        val bottomWidth = size.width + 2 * bottomOverlap
        val topStart = 0f
        val topEnd = topStart + topWidth
        val bottomStart = -bottomOverlap
        val bottomEnd = bottomStart + bottomWidth
        val radius = min(cornerRadius, min(topWidth / 4f, size.height / 2f))
        val path = Path().apply {
            moveTo(topStart, radius)
            arcTo(
                rect = androidx.compose.ui.geometry.Rect(
                    left = topStart,
                    top = 0f,
                    right = topStart + radius * 2,
                    bottom = radius * 2
                ),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            lineTo(topEnd - radius, 0f)
            arcTo(
                rect = androidx.compose.ui.geometry.Rect(
                    left = topEnd - radius * 2,
                    top = 0f,
                    right = topEnd,
                    bottom = radius * 2
                ),
                startAngleDegrees = 270f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            lineTo(bottomEnd, size.height)
            lineTo(bottomStart, size.height)
            lineTo(topStart, radius)
            close()
        }
        return Outline.Generic(path)
    }
}

@Composable
fun CustomTabs(selectedTab: SwapTabsEnum, onTabSelected: (SwapTabsEnum) -> Unit) {
    val tabs = listOf(SwapTabsEnum.PHOTO, SwapTabsEnum.VIDEO/*, SwapTabsEnum.CLOTHES*/)
    val overlap = 16.dp
    val density = LocalDensity.current.density
    val overlapPx = overlap.value * density
    Row(
        Modifier.fillMaxWidth()
    ) {
        tabs.forEachIndexed { index, item ->
            val isSelected = selectedTab == tabs[index]
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .zIndex(if (isSelected) 1f else 0f)
                    .clip(TrapezoidShape(cornerRadius = 24f, bottomOverlap = overlapPx))
                    .background(
                        if (isSelected) Color(0xFF18191C) else Color(0xFF5A5B5B),
                        shape = TrapezoidShape(cornerRadius = 24f, bottomOverlap = overlapPx)
                    )
                    .clickable { onTabSelected(tabs[index]) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = if (isSelected) Color.Unspecified else Color(0xFF111212),
                    style = if (isSelected) androidx.compose.ui.text.TextStyle(
                        brush = Brush.horizontalGradient(
                            listOf(Color(0xFF9D9FE7), Color(0xFF9DE7A6))
                        )
                    ) else LocalTextStyle.current
                )
            }
        }
    }
}