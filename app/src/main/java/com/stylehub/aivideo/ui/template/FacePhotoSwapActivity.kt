package com.stylehub.aivideo.ui.template

import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.stylehub.aivideo.R
import com.stylehub.aivideo.base.BaseActivity
import com.stylehub.aivideo.network.model.out.RecommendHeadListRespDataModel
import com.stylehub.aivideo.network.model.out.Template
import com.stylehub.aivideo.ui.dialog.ChooseFaceDialog
import com.stylehub.aivideo.ui.dialog.CommonProgressDialog
import com.stylehub.aivideo.utils.GalleryUtil
import com.stylehub.aivideo.utils.ScreenUtil.dp2px

/**
 *
 * Create by league at 2025/7/1
 *
 * Write some description here
 */
class FacePhotoSwapActivity : BaseActivity<FacePhotoSwapActivityViewModel, FacePhotoSwapActivityData>() {
    override val mViewModel: FacePhotoSwapActivityViewModel by viewModels()

    companion object {
        const val EXTRA_TEMPLATE = "template"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.setIntentData(intent.getSerializableExtra(EXTRA_TEMPLATE) as Template?)
    }

    @Composable
    override fun ProvideContent(uiStateData: FacePhotoSwapActivityData) {
        FacePhotoSwapScreen(
            this,
            uiStateData = uiStateData,
            onRecommendHeadSelect = {
                //选择了模板图
                mViewModel.onRecommendSelected(it)
            },
            onAddFromAlbum = {
                //选择了相册中的图片
                GalleryUtil.openGallery(this, GalleryUtil.MediaType.IMAGE) {
                        uri ->
                    mViewModel.onAddFromAlbum(uri)
                }
            },
            onDeleteSelectHead = {
                //点击了删除所选
                mViewModel.onDeleteSelectedHead()

            },
            onSwapClick = {
                //点击了换脸按钮
                mViewModel.swapFace()
            }
        )
        
        // 显示进度对话框
        CommonProgressDialog(
            show = uiStateData.showProgressDialog,
            progress = uiStateData.progress / 100f, // 转换为0-1范围
            text = uiStateData.progressText
        )
    }
}

@Composable
fun FacePhotoSwapScreen(
    activity: FacePhotoSwapActivity,
    uiStateData: FacePhotoSwapActivityData,
    onRecommendHeadSelect: (headInfo: RecommendHeadListRespDataModel.HeadInfo) -> Unit = {},
    onAddFromAlbum: () -> Unit = {},
    onDeleteSelectHead: () -> Unit = {},
    onSwapClick: () -> Unit = {}
) {
    val step = uiStateData.step
    val swapResult = uiStateData.swapResult
    val composedBitmap = uiStateData.composeBitmap
    val templateHeadBitmap = uiStateData.templateHeadBitmap
    val credits = uiStateData.credits
    val selectedHeadBitmap = uiStateData.selectHeadBitmap
    val recommendHeadList = uiStateData.recommendData?.sys?: emptyList()
    val progress = uiStateData.progress
    val progressText = uiStateData.progressText

    val showChooseFaceDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF111212))
            .padding(WindowInsets.statusBars.asPaddingValues())
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
                IconButton(onClick = { activity.finish() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            }
            Text(
                text = "Template",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        // 模板大图（合成高亮框后直接显示）
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 30.dp, top = 30.dp, end = 30.dp)
                .clip(RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (composedBitmap != null) {
                Image(
                    bitmap = composedBitmap.asImageBitmap(),
                    contentDescription = "Template with highlight",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            if (step == FacePhotoSwapActivityData.Step.Swapping) {

                // 合成中状态布局
                Box {
                    //底层模糊背景，相当与边框
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .blur(20.dp)
                            .shadow(4.dp, RoundedCornerShape(20.dp), ambientColor = Color(0xCC9D9FE7), spotColor = Color(0xCC9D9FE7))
                            .background(brush = Brush.horizontalGradient(colors = listOf(Color(0xCC9D9FE7), Color(0xDD9DE7A6))))
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
                                painter = if (templateHeadBitmap != null) rememberAsyncImagePainter(templateHeadBitmap)
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
                                painter = if (selectedHeadBitmap != null) rememberAsyncImagePainter(selectedHeadBitmap)
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
                            // 背景
                            Image(
                                painter = painterResource(id = R.drawable.bg_progress_bar),
                                contentDescription = null,
                                modifier = Modifier.matchParentSize(),
                                contentScale = ContentScale.FillBounds
                            )
                            // 渐变进度
                            Canvas(modifier = Modifier.matchParentSize()) {
                                val progressWidth = size.width * (progress / 100f)
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
                            text = "${progress}%",
                            color = Color.White,
                            fontSize = 16.sp,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.height(35.dp))
                        Text(
                            text = progressText,
                            color = Color.White,
                            fontSize = 16.sp,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }


            }
        }


        Column (
            modifier = Modifier.fillMaxWidth()
                .height(140.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            if (credits != null) {

                Spacer(modifier = Modifier.height(5.dp))
                // 积分
                Text(
                    text = "$credits credits",
                    color = Color.White,
                    fontSize = 13.sp,
                )
            }

            Spacer(Modifier.height(39.dp))

            // 底部操作区
            if (step == FacePhotoSwapActivityData.Step.ChoseFace || step == FacePhotoSwapActivityData.Step.BeforeSwap) {
                //选择脸部模板或者照片，在点击换脸按钮之前

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max)
                ) {

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(Modifier.padding(horizontal = 30.dp)) {
                            // 已选头像
                            Image(
                                painter = if (templateHeadBitmap != null) rememberAsyncImagePainter(
                                    model = templateHeadBitmap
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
                                modifier = Modifier.size(24.dp).align(Alignment.CenterVertically)
                            )
                            Spacer(modifier = Modifier.width(5.dp))

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                                val uploadBoxModifier = Modifier
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { showChooseFaceDialog.value = true }

                                if (selectedHeadBitmap == null) {
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
                                        painter = if (selectedHeadBitmap != null) rememberAsyncImagePainter(
                                            selectedHeadBitmap
                                        ) else painterResource(id = R.drawable.ic_add),
                                        contentDescription = "Upload",
                                        modifier = Modifier.size(49.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                }

                                if (selectedHeadBitmap != null) {

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

                        if (selectedHeadBitmap == null) {

                            Spacer(Modifier.height(5.dp))
                            // 上传提示
                            Text(
                                text = "Click \"+\" to upload a face photo",
                                color = Color.White,
                                fontSize = 10.sp,
                                modifier = Modifier.height(24.dp).padding(horizontal = 5.dp)
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
            } else {
                //其他状态下只有一个下载按钮

                var boxModifier = Modifier
                    .fillMaxWidth(0.3f)
                    .height(50.dp)
                    .clip(RoundedCornerShape(10.dp))

                val textColor:Color

               if (swapResult == FacePhotoSwapActivityData.SwapResult.Success) {
                   textColor = Color.Black
                   boxModifier = boxModifier.background(brush = Brush.linearGradient(listOf(Color(0xFF9D9FE7), Color(0xFF9DE7A6))), RoundedCornerShape(10.dp))
               } else {
                   textColor = Color(0xFF5A5B5B)
                   boxModifier = boxModifier.border(1.dp, Color.White, RoundedCornerShape(10.dp))
               }

                // 下载按钮（禁用）
                Box(
                    modifier = boxModifier,
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Download",
                        color = textColor,
                        fontSize = 15.sp
                    )
                }
            }
        }



    }
    // 弹窗调用
    ChooseFaceDialog(
        show = showChooseFaceDialog.value,
        onDismiss = { showChooseFaceDialog.value = false },
        onAddFromAlbum = onAddFromAlbum,
        faceList = recommendHeadList,
        onRecommendHeadSelect = onRecommendHeadSelect,
    )
}
