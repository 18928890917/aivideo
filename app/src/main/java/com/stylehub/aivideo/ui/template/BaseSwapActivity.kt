package com.stylehub.aivideo.ui.template

import android.os.Bundle
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.stylehub.aivideo.R
import com.stylehub.aivideo.base.BaseActivity
import com.stylehub.aivideo.network.model.out.Template
import com.stylehub.aivideo.ui.common.ExoVideoPlayer
import com.stylehub.aivideo.ui.dialog.ChooseFaceDialog
import com.stylehub.aivideo.ui.dialog.CommonProgressDialog
import com.stylehub.aivideo.ui.dialog.FirstUploadHintDialog
import com.stylehub.aivideo.ui.dialog.WaitingTooLongDialog
import com.stylehub.aivideo.utils.GalleryUtil
import com.stylehub.aivideo.utils.LoginManager
import com.stylehub.aivideo.utils.ScreenUtil.dp2px

/**
 *
 * Create by league at 2025/7/1
 *
 * Write some description here
 */
abstract class BaseSwapActivity<VM : BaseSwapActivityViewModel<D>, D : BaseSwapActivityUiData> :
    BaseActivity<BaseSwapActivityViewModel<D>, D>() {

    companion object {
        const val EXTRA_TEMPLATE = "template"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.setIntentData(intent.getSerializableExtra(EXTRA_TEMPLATE) as Template?)
    }

    override fun onBackPressed() {
        if (!mViewModel.onBackPressed()) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LoginManager.updateUserAccount()
    }

    @Composable
    override fun ProvideContent(uiStateData: D) {

        //主页面
        SwapScreen(
            uiStateData = uiStateData,
            onBackClick = {
                if (!mViewModel.onBackPressed()) {
                    finish()
                }
            },
            onAddButtonClick = { mViewModel.showSelectPhotoDialog() },
            onDeleteSelectHead = { mViewModel.onDeleteSelectedPhoto() },
            onSwapClick = { mViewModel.onSwapClick() },
            onDownloadClick = { mViewModel.onDownloadClick() },
            onSwapClick2 = { r: String?, c: Int? -> mViewModel.onSwapClick(r, c) },
            onSkipClick = { mViewModel.onSkipClick() },
        )

        //进度弹窗
        CommonProgressDialog(
            show = uiStateData.showUploadDialog,
            progress = uiStateData.uploadProgress / 100f,
            text = uiStateData.uploadDialogHint
        )

        //选择人脸或者是相册的弹窗
        ChooseFaceDialog(
            show = uiStateData.showSelectPhotoDialog,
            onDismiss = { mViewModel.dismissSelectPhotoDialog() },
            onAddFromAlbum = {
                //选择了相册中的图片
                GalleryUtil.openGallery(this, GalleryUtil.MediaType.IMAGE) { uri ->
                    mViewModel.onSelectPhoto(uri)
                }
            },
            faceList = uiStateData.recommendData?.sys ?: emptyList(),
            onRecommendHeadSelect = { mViewModel.onRecommendHeadSelected(it) }
        )

        //等待过久弹窗
        WaitingTooLongDialog(
            show = uiStateData.showWaitingTooLongDialog,
            onDismiss = { mViewModel.dismissWaitingTooLongDialog() },
            onRunInTheBackgroundClick = { finish() }
        )

        //首次上传提示弹窗
        FirstUploadHintDialog(
            show = uiStateData.showFirstUploadHintDialog,
            onDismiss = { mViewModel.dismissFirstUploadHintDialog() }
        )

    }
}

@Composable
fun <D : BaseSwapActivityUiData> SwapScreen(
    uiStateData: D,
    onBackClick: () -> Unit = {},
    onAddButtonClick: () -> Unit = {},
    onDeleteSelectHead: () -> Unit = {},
    onSwapClick: () -> Unit = {},
    onDownloadClick: () -> Unit = {},
    onSwapClick2: (resolution: String?, credits: Int?) -> Unit = { _, _ -> },
    onSkipClick: () -> Unit = {}
) {
    Scaffold {
            padding:PaddingValues->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF111212))
                .padding(padding)
                .padding(bottom = 25.dp)
        ) {

            // 顶部栏
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                }
                Text(
                    text = uiStateData.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            uiStateData.subTitle?.run {
                // 副页面标题
                Text(
                    text = this,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 29.dp, top = 36.dp, bottom = 22.dp)
                )
            }

            // 中间区域
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(start = 30.dp, top = 30.dp, end = 30.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                //中间图片内容
                MiddlePicContent(uiStateData)

                //中部进度组件
                MiddleProgressContent(uiStateData)

                //合成结果
                MiddleSwapResultContent(uiStateData)

            }


            //底部区域
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
            ) {

                //带底部栏的底部
                BottomBarWithTemplate(
                    uiStateData = uiStateData,
                    onAddButtonClick = onAddButtonClick,
                    onDeleteSelectHead = onDeleteSelectHead,
                    onSwapClick = onSwapClick
                )

                //单按钮底部
                BottomBarWithSingleButton(
                    uiStateData = uiStateData,
                    onDownloadClick = onDownloadClick,
                    onUploadPhotoClick = onAddButtonClick,
                    onStartMakingClick = onSwapClick,
                    onSkipClick = onSkipClick
                )

                //进度条底部
                BottomBarWithProgress(
                    uiStateData = uiStateData
                )

                //带分辨率选择底部
                BottomBarWithResolution(
                    uiStateData = uiStateData,
                    onStartMakingClick = onSwapClick2
                )
            }

        }
    }

}

/**
 * 中间图片内容
 */
@Composable
fun <D : BaseSwapActivityUiData> MiddlePicContent(uiStateData: D) {

    if (uiStateData.showMiddlePic) {

        if (uiStateData.largeImageBitmap == null) {

            if (uiStateData.successVideoUrl != null) {
                //视频-通过视频url设置
                uiStateData.successVideoUrl?.run {
                    ExoVideoPlayer(url = this, modifier = Modifier.fillMaxSize())
                }
            } else {
                //大图-通过url设置
                uiStateData.largeImageUrl?.run {
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = this,
                        contentDescription = "Large image",
                        contentScale = ContentScale.Crop
                    )
                }

                //视频-通过视频url设置
                uiStateData.largeVideoUrl?.run {
                    ExoVideoPlayer(url = this, modifier = Modifier.fillMaxSize())
                }
            }
        }

        //大图-通过bitmap直接设置
        uiStateData.largeImageBitmap?.run {
            Image(
                bitmap = this.asImageBitmap(),
                contentDescription = "Template with highlight",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

    }
}

/**
 * 中间进度内容
 */
@Composable
fun <D : BaseSwapActivityUiData> MiddleProgressContent(uiStateData: D) {

    if (!uiStateData.showMiddleProgressContent)
        return

    // 合成中状态布局
    Box {
        //底层模糊背景，相当与边框
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(20.dp)
                .shadow(
                    4.dp,
                    RoundedCornerShape(20.dp),
                    ambientColor = Color(0xCC9D9FE7),
                    spotColor = Color(0xCC9D9FE7)
                )
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xCC9D9FE7),
                            Color(0xDD9DE7A6)
                        )
                    )
                )
                .clip(RoundedCornerShape(20.dp))
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(1.dp)
                .background(Color(0xFF111212), RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .padding(vertical = 32.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(160.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                // 左侧头像（模板头像）
                Image(
                    painter = if (uiStateData.middleContentLeftHeadBitmap != null) rememberAsyncImagePainter(
                        uiStateData.middleContentLeftHeadBitmap
                    )
                    else painterResource(id = R.drawable.ic_profile_default),
                    contentDescription = "Template Face",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(24.dp))
                )
                Spacer(modifier = Modifier.width(24.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(24.dp))
                // 右侧头像（用户选择头像）
                Image(
                    painter = if (uiStateData.middleContentRightHeadBitmap != null) rememberAsyncImagePainter(
                        uiStateData.middleContentRightHeadBitmap
                    )
                    else painterResource(id = R.drawable.ic_profile_default),
                    contentDescription = "Selected Face",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(24.dp))
                )
            }
            Spacer(modifier = Modifier.height(100.dp))
            // 进度条
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(18.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.CenterStart
            ) {

                val infiniteTransition = rememberInfiniteTransition(label = "")
                var imageWidth by remember { mutableIntStateOf(0) }
                val animation by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = -imageWidth.toFloat(), // 移动的距离，负值表示向左移动（视觉上是背景向右流动）
                    animationSpec = InfiniteRepeatableSpec(
                        animation = tween(
                            durationMillis = 4000, // 动画时间，越小越快
                            easing = LinearEasing // 线性运动，不加速不减速
                        )
                    ), label = ""
                )

                // 背景
                Box(modifier = Modifier.matchParentSize().onSizeChanged { imageWidth = it.width }) {
                    (0..1).forEach { i -> // 复制背景图以确保无缝滚动
                        Image(
                            painter = painterResource(id = R.drawable.bg_progress_bar2),
                            contentDescription = null,
                            modifier = Modifier
                                .matchParentSize()
                                .graphicsLayer {
                                    translationX = animation + (i * imageWidth) // 根据i值调整translationX
                                },
                            contentScale = ContentScale.FillBounds
                        )
                    }
                }
                // 渐变进度
                Canvas(modifier = Modifier.matchParentSize()) {
                    val progressWidth = size.width * (uiStateData.middleContentProgress / 100f)
                    if (progressWidth > 0f) {
                        drawRoundRect(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFF9D9FE7), Color(0xFF9DE7A6))
                            ),
                            topLeft = Offset.Zero,
                            size = Size(progressWidth, size.height),
                            cornerRadius = CornerRadius(12.dp2px().toFloat(), 12.dp2px().toFloat())
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "${uiStateData.middleContentProgress}%",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(35.dp))
            Text(
                text = uiStateData.middleContentHint,
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }


}

/**
 * 合成结果
 */
@Composable
fun <D : BaseSwapActivityUiData> MiddleSwapResultContent(uiStateData: D) {

    if (uiStateData.showMiddleFail || uiStateData.showMiddleLoading) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            val painter: Painter
            val hint: String

            if (uiStateData.showMiddleFail) {
                painter = painterResource(R.mipmap.ic_fail_hint)
                hint = "Failed"
            } else {
                painter = painterResource(R.mipmap.ic_loading_hint)
                hint = "Loading..."
            }

            Image(
                painter = painter,
                contentDescription = "swap result",
                contentScale = ContentScale.Crop
            )

            Text(
                text = hint,
                fontSize = 15.sp,
                color = Color(0xFF5A5B5B)
            )

        }
    }
}

/**
 * 带模板显示的底部
 */
@Composable
fun <D : BaseSwapActivityUiData> BottomBarWithTemplate(
    uiStateData: D,
    onAddButtonClick: () -> Unit = {},
    onDeleteSelectHead: () -> Unit = {},
    onSwapClick: () -> Unit = {}
) {

    if (!uiStateData.showBottomBarWithTemplate)
        return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Box(
            Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(top = 5.dp),
            contentAlignment = Alignment.TopCenter
        ) {

            if (uiStateData.showCredits) {
                // 积分
                Text(
                    text = "${uiStateData.credits} credits",
                    color = Color.White,
                    fontSize = 13.sp,
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(Modifier.padding(horizontal = 30.dp)) {
                    // 已选头像
                    Image(
                        painter = if (uiStateData.bottomBarLeftHeadBitmap != null) rememberAsyncImagePainter(
                            model = uiStateData.bottomBarLeftHeadBitmap
                        ) else painterResource(id = R.drawable.ic_profile_default),
                        contentDescription = "Face Photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(10.dp)),
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(5.dp))

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        val uploadBoxModifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable(onClick = onAddButtonClick)

                        if (uiStateData.bottomBarRightHeadBitmap == null) {
                            uploadBoxModifier.border(
                                1.dp,
                                Color.White,
                                RoundedCornerShape(10.dp)
                            )
                        }

                        // 上传按钮
                        Box(
                            modifier = uploadBoxModifier,
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = if (uiStateData.bottomBarRightHeadBitmap != null) rememberAsyncImagePainter(
                                    uiStateData.bottomBarRightHeadBitmap
                                ) else painterResource(id = R.drawable.ic_add),
                                contentDescription = "Upload",
                                modifier = Modifier.size(49.dp),
                                contentScale = ContentScale.Crop
                            )
                        }

                        if (uiStateData.bottomBarRightHeadBitmap != null) {

                            Spacer(Modifier.height(5.dp))

                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable(onClick = onDeleteSelectHead),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_delete),
                                    contentDescription = "Upload",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }

                if (uiStateData.bottomBarRightHeadBitmap == null) {

                    Spacer(Modifier.height(5.dp))
                    // 上传提示
                    Text(
                        text = "Click \"+\" to upload a face photo",
                        color = Color.White,
                        fontSize = 10.sp,
                        modifier = Modifier
                            .height(24.dp)
                            .padding(horizontal = 5.dp)
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            // Swap 按钮
            Box(
                modifier = Modifier
                    .widthIn(min = 150.dp)
                    .height(50.dp)
                    .padding(start = 19.dp, end = 19.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF9D9FE7), Color(0xFF9DE7A6)
                            )
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable(onClick = onSwapClick),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Swap face",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center
                )
            }

        }

    }
}

/**
 * 只有单一按钮的底部
 */
@Composable
fun <D : BaseSwapActivityUiData> BottomBarWithSingleButton(
    uiStateData: D,
    onDownloadClick: () -> Unit = {},
    onUploadPhotoClick: () -> Unit = {},
    onStartMakingClick: () -> Unit = {},
    onSkipClick: () -> Unit = {}
) {

    if (!uiStateData.showBottomBarWithSingleButton)
        return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(25.dp),
            contentAlignment = Alignment.Center
        ) {
            if (uiStateData.showUploadPhotoButton || uiStateData.showStartMakingButton) {

                if (uiStateData.showCredits) {
                    // 积分
                    Text(
                        text = "${uiStateData.credits} credits",
                        color = Color.White,
                        fontSize = 13.sp,
                    )
                }
            }
        }


        //下载按钮
        if (uiStateData.showDownloadButton) {

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .height(50.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable(onClick = onDownloadClick)
                    .background(
                        brush = Brush.linearGradient(
                            listOf(
                                Color(0xFF9D9FE7),
                                Color(0xFF9DE7A6)
                            )
                        ), RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Download",
                    color = Color.Black,
                    fontSize = 15.sp
                )
            }

        }

        //下载按钮（禁用）
        if (uiStateData.showDownloadButtonDisable) {

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .height(50.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, Color.White, RoundedCornerShape(10.dp))
                    .clickable(onClick = onDownloadClick),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Download",
                    color = Color(0xFF5A5B5B),
                    fontSize = 15.sp
                )
            }
        }

        //上传按钮
        if (uiStateData.showUploadPhotoButton) {
            // 底部上传按钮
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                //下载按钮
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFFB0BFFF), Color(0xFFBFFFD0))
                            )
                        )
                        .clickable(onClick = onUploadPhotoClick)
                        .padding(horizontal = 32.dp, vertical = 12.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_photo),
                            contentDescription = "Upload",
                            tint = Color.Black,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "upload photo",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }

                if (uiStateData.showSkipRightOfUploadPhotoButton) {

                    Box(
                        modifier = Modifier
                            .clickable(onClick = onSkipClick)
                            .padding(horizontal = 32.dp, vertical = 12.dp)
                            .align(Alignment.CenterEnd)
                    ) {
                        Text(
                            text = "Skip",
                            color = Color.White,
                            fontSize = 15.sp
                        )
                    }

                }
            }
        }

        //开始制作按钮
        if (uiStateData.showStartMakingButton) {

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(50.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable(onClick = onStartMakingClick)
                    .background(
                        brush = Brush.linearGradient(
                            listOf(
                                Color(0xFF9D9FE7),
                                Color(0xFF9DE7A6)
                            )
                        ), RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Start making",
                    color = Color.Black,
                    fontSize = 15.sp
                )
            }

        }

    }
}

/**
 * 进度条底部
 */
@Composable
fun <D : BaseSwapActivityUiData> BottomBarWithProgress(uiStateData: D) {

    if (!uiStateData.showBottomBarWithProgressBar)
        return

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        // 进度条
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(20.dp)
                .clip(RoundedCornerShape(18.dp)),
            contentAlignment = Alignment.CenterStart
        ) {

            val infiniteTransition = rememberInfiniteTransition(label = "")
            var imageWidth by remember { mutableIntStateOf(0) }
            val animation by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = -imageWidth.toFloat(), // 移动的距离，负值表示向左移动（视觉上是背景向右流动）
                animationSpec = InfiniteRepeatableSpec(
                    animation = tween(
                        durationMillis = 4000, // 动画时间，越小越快
                        easing = LinearEasing // 线性运动，不加速不减速
                    )
                ), label = ""
            )

            // 背景
            Box(modifier = Modifier.matchParentSize().onSizeChanged { imageWidth = it.width }) {
                (0..1).forEach { i -> // 复制背景图以确保无缝滚动
                    Image(
                        painter = painterResource(id = R.drawable.bg_progress_bar2),
                        contentDescription = null,
                        modifier = Modifier
                            .matchParentSize()
                            .graphicsLayer {
                                translationX = animation + (i * imageWidth) // 根据i值调整translationX
                            },
                        contentScale = ContentScale.FillBounds
                    )
                }
            }

            // 渐变进度
            Canvas(modifier = Modifier.matchParentSize()) {
                val progressWidth = size.width * (uiStateData.bottomBarProgress / 100f)
                if (progressWidth > 0f) {
                    drawRoundRect(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF9D9FE7), Color(0xFF9DE7A6))
                        ),
                        topLeft = Offset.Zero,
                        size = Size(progressWidth, size.height),
                        cornerRadius = CornerRadius(12.dp2px().toFloat(), 12.dp2px().toFloat())
                    )
                }
            }
        }
    }


}

/**
 * 带分辨率选择的底部
 */
@Composable
fun <D : BaseSwapActivityUiData> BottomBarWithResolution(
    uiStateData: D,
    onStartMakingClick: (resolution: String?, credits: Int?) -> Unit = { _, _ -> }
) {

    if (!uiStateData.showBottomBarWithResolution)
        return

    var key: String? by remember { mutableStateOf(uiStateData.resolutionMap?.keys?.first()) }
    var value: Int? by remember { mutableStateOf(uiStateData.resolutionMap?.values?.first()) }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp),
        verticalAlignment = Alignment.Bottom
    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                modifier = Modifier.padding(5.dp),
                text = "Resolution",
                color = Color.White,
                fontSize = 16.sp
            )

            Row(
                Modifier
                    .height(50.dp)
            ) {
                uiStateData.resolutionMap?.run {

                    if (this.isNotEmpty()) {

                        this.forEach { (resolution, credits) ->

                            Column(
                                modifier = Modifier
                                    .width(92.dp)
                                    .height(50.dp)
                                    .background(Color(0xFF1D1D1D), RoundedCornerShape(10.dp))
                                    .clip(RoundedCornerShape(10.dp))
                                    .border(
                                        width = 2.dp,
                                        brush = Brush.linearGradient(
                                            if (key == resolution) {
                                                listOf(
                                                    Color(0xFF9D9FE7),
                                                    Color(0xFF9DE7A6)
                                                )
                                            } else {
                                                listOf(
                                                    Color(0xFF1D1D1D),
                                                    Color(0xFF1D1D1D),
                                                )
                                            }
                                        ),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable {
                                        key = resolution
                                        value = credits
                                    },
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = resolution,
                                    fontSize = 13.sp,
                                    color = if (key == resolution) Color.White else Color(0xFF5A5B5B),
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                                Spacer(Modifier.weight(1f))
                                Text(
                                    text = "$credits Credits",
                                    fontSize = 13.sp,
                                    color = if (key == resolution) Color.White else Color(0xFF5A5B5B),
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                            }
                        }
                    }
                }
            }

        }

        Box(
            modifier = Modifier
                .width(150.dp)
                .height(50.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            Color(0xFF9D9FE7),
                            Color(0xFF9DE7A6)
                        )
                    ), RoundedCornerShape(10.dp)
                )
                .clickable { onStartMakingClick(key, value) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Start making",
                color = Color.Black,
                fontSize = 16.sp
            )
        }

    }

}