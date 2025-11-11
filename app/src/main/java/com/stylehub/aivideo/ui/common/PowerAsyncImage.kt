package com.stylehub.aivideo.ui.common

import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.load

/**
 *
 * Create by league at 2025/11/10
 *
 * Write some description here
 */
@Composable
fun PowerAsyncImage(
    model: Any?,
    modifier: Modifier = Modifier,
    placeholder: Drawable? = null,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Fit
) {
    val context = LocalContext.current

    // 创建支持GIF的ImageLoader，使用remember避免重复创建
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                add(GifDecoder.Factory())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    add(ImageDecoderDecoder.Factory())
                }
            }
            .placeholder(placeholder)
            .build()
    }

    AndroidView(
        factory = { ctx ->
            ImageView(ctx).apply {
                this.contentDescription = contentDescription
                scaleType = when (contentScale) {
                    ContentScale.Crop -> ImageView.ScaleType.CENTER_CROP
                    ContentScale.Fit -> ImageView.ScaleType.FIT_CENTER
                    ContentScale.FillBounds -> ImageView.ScaleType.FIT_XY
                    ContentScale.FillHeight -> ImageView.ScaleType.FIT_XY
                    ContentScale.FillWidth -> ImageView.ScaleType.FIT_XY
                    ContentScale.Inside -> ImageView.ScaleType.FIT_CENTER
                    ContentScale.None -> ImageView.ScaleType.CENTER
                    else -> ImageView.ScaleType.FIT_CENTER
                }
            }
        },
        modifier = modifier,
        update = { imageView ->
            imageView.load(model, imageLoader) {
                crossfade(true)
            }
        }
    )
}