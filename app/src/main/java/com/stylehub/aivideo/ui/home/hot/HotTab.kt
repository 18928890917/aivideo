package com.stylehub.aivideo.ui.home.hot

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.stylehub.aivideo.R
import com.stylehub.aivideo.network.model.out.ClothesTemplateRespDataModel
import com.stylehub.aivideo.network.model.out.CreateImageTaskTemplateModel
import com.stylehub.aivideo.network.model.out.FaceSwapVideoTemplateRespDataModel
import com.stylehub.aivideo.network.model.out.GetImageTemplateRespDataModel
import com.stylehub.aivideo.network.model.out.UserConfigDataModel
import com.stylehub.aivideo.ui.common.CommonEmptyView
import com.stylehub.aivideo.ui.home.HomeActivity
import com.stylehub.aivideo.ui.home.SwapTabsEnum
import com.stylehub.aivideo.utils.AppRouterManager
import kotlinx.coroutines.launch

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun HotTab(
    innerPadding: PaddingValues,
    homeActivity: HomeActivity,
    onTabRequest: (tab: String) -> Unit
) {

    val viewModel = homeActivity.mViewModel
    val hotTabData = viewModel.uiStateData.value.hotTabData

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val showFab by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0
        }
    }

    // 当切换到DanceTab时加载数据
    LaunchedEffect(Unit) {
        viewModel.getUserCommonInfo()
    }

    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF131313))
                .padding(innerPadding)
                .padding(horizontal = 5.dp),
            verticalArrangement = Arrangement.spacedBy(27.dp)
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }

            // 显示加载状态
            if (hotTabData.isLoading) {
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

            if (hotTabData.configList.isNotEmpty()) {

                items(hotTabData.configList) { item: UserConfigDataModel ->
                    HotCategoryCard(
                        title = item.title,
                        imageUrl = item.imgUrl,
                        description = item.description,
                        onClick = {

                            when (item.genType) {

                                1 -> {
                                    //图片换脸
                                    val template : GetImageTemplateRespDataModel? = item.getTemplateModel()
                                    if (template != null) {
                                        AppRouterManager.enterImageFaceSwapActivity(template = template)
                                    } else {
                                        onTabRequest("swap")
                                        viewModel.switchTab(SwapTabsEnum.PHOTO)
                                    }
                                }
                                2 -> {
                                    //视频换脸
                                    val template : FaceSwapVideoTemplateRespDataModel? = item.getTemplateModel()
                                    if (template != null) {
                                        AppRouterManager.enterVideoFaceSwapActivity(template = template)
                                    } else {
                                        onTabRequest("swap")
                                        viewModel.switchTab(SwapTabsEnum.VIDEO)
                                    }
                                }
                                3 -> {
                                    //换衣
                                    val template : ClothesTemplateRespDataModel? = item.getTemplateModel()
                                    if (template != null) {
                                        AppRouterManager.enterClothesSwapActivity(template = template)
                                    } else {
                                        onTabRequest("clothes swap")
                                    }
                                }
                                4 -> {
                                    //黏土风格
                                    AppRouterManager.enterClayStylePhotoSwapActivity()
                                }
                                else -> {
                                    //通用生图任务
                                    val template : CreateImageTaskTemplateModel? = item.getTemplateModel()
                                    if (template != null) {
                                        AppRouterManager.enterCommonSwapActivity(template = template)
                                    }
                                }

                            }
                        }
                    )

                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }

        //无数据
        if (!hotTabData.isLoading && hotTabData.configList.isEmpty()) {
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

@Composable
fun HotCategoryCard(
    title: String = "",
    imageUrl: String = "",
    imageRes: Int = R.mipmap.bg_hot_clay_style,
    description: String = "",
    onClick: () -> Unit = {}
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

        Text(
            modifier = Modifier.padding(start = 10.dp),
            text = title,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Card(
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
        ) {
            Box(contentAlignment = Alignment.BottomCenter) {

                if (imageUrl.isEmpty()) {

                    Image(
                        painter = painterResource(imageRes),
                        contentDescription = title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(355f / 242f),
                        contentScale = ContentScale.Crop
                    )

                } else {

                    AsyncImage(
                        model = imageUrl,
                        contentDescription = title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(355f / 242f),
                        contentScale = ContentScale.Crop
                    )
                }

                ActionRow(description)
            }
        }
    }
}

@Composable
private fun ActionRow(description: String) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF111212).copy(alpha = 0.7f), RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .padding(start = 13.dp, top = 10.dp, end = 17.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = description, color = Color.White, fontSize = 15.sp)
        Image(
            painter = painterResource(R.mipmap.ic_arrow_right_fill_with_black_circle),
            contentDescription = "Go",
            modifier = Modifier.size(50.dp)
        )
    }
}