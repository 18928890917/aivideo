package com.stylehub.aivideo.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.util.Base64
import androidx.core.content.res.ResourcesCompat
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.stylehub.aivideo.AiSwapApplication
import com.stylehub.aivideo.R
import java.io.ByteArrayOutputStream

/*
 * ImageUtil
 *
 * 用于Compose中加载网络图片，基于Coil实现。
 *
 * 使用方法：
 * ImageUtil.Load(
 *     url = "https://example.com/image.jpg",
 *     modifier = Modifier.size(100.dp),
 *     contentDescription = "网络图片"
 * )
 */

object ImageUtil {

    private fun getContext(): Context {
        return AiSwapApplication.getInstance().getTopActivity() ?: AiSwapApplication.getInstance()
    }

    /**
     * 获取网络图片bitmap
     */
    suspend fun getBitmap(uri: Uri?): Bitmap? {

        val context = getContext()
        val loader = context.imageLoader
        val request = ImageRequest.Builder(context)
            .data(uri)
            .allowHardware(false)
            .build()
        val result = loader.execute(request)
        if (result is SuccessResult) {
            val srcBitmap =
                (result.drawable as? android.graphics.drawable.BitmapDrawable)?.bitmap
            return srcBitmap
        }
        return null
    }

    /**
     * 获取资源图片实例
     * @param context   上下文
     * @param res       资源id
     * @return          图片实例
     */
    fun getBitmap(context: Context, res: Int): Bitmap? {
        return getBitmap(context.getDrawable(res))
    }


    /**
     * 根据头像图片以及头像坐标获取头像数据
     */
    fun getHeaderBitmap(srcBitmap: Bitmap?, headPos: String?): Bitmap? {

        if (srcBitmap == null) {
            return null
        }

        val arr = headPos?.split(",")?.mapNotNull { it.toFloatOrNull() }
        val headBox = if (arr != null && arr.size == 4) {
            val (x1, y1, x2, y2) = arr
            HeadBox(x1, y1, x2, y2)
        } else null

        if (headBox == null)
            return null

        // 1. 先裁剪头像
        val headBoxWidth = (headBox.x2 - headBox.x1)
        val cropSize = (headBoxWidth * 2).toInt()
        val width = srcBitmap.width
        val height = srcBitmap.height
        val centerX = ((headBox.x1 + headBox.x2) / 2f)
        val centerY = ((headBox.y1 + headBox.y2) / 2f)
        var cropLeft = (centerX - cropSize / 2).toInt()
        var cropTop = (centerY - cropSize / 2).toInt()
        // 边界检查
        if (cropLeft < 0) cropLeft = 0
        if (cropTop < 0) cropTop = 0
        if (cropLeft + cropSize > width) cropLeft = width - cropSize
        if (cropTop + cropSize > height) cropTop = height - cropSize
        // 防止负数
        if (cropLeft < 0) cropLeft = 0
        if (cropTop < 0) cropTop = 0
        val actualCropSize = minOf(cropSize, width - cropLeft, height - cropTop)
        return try {
            Bitmap.createBitmap(srcBitmap, cropLeft, cropTop, actualCropSize, actualCropSize)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 根据头像图片以及头像坐标合成边框包裹头像的图片
     */
    fun composeHeadBoxIntoBitmap(srcBitmap: Bitmap?, headPos: String?): Bitmap? {

        if (srcBitmap == null) {
            return null
        }

        val arr = headPos?.split(",")?.mapNotNull { it.toFloatOrNull() }
        val headBox = if (arr != null && arr.size == 4) {
            val (x1, y1, x2, y2) = arr
            HeadBox(x1, y1, x2, y2)
        } else null

        if (headBox == null)
            return null

        val context = getContext()

        // 合成高亮框
        val outBitmap = srcBitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(outBitmap)
        val res = context.resources
        val boxDrawable = ResourcesCompat.getDrawable(res, R.drawable.bg_header_box, context.theme)
        if (boxDrawable != null) {
            boxDrawable.setBounds(
                headBox.x1.toInt(),
                headBox.y1.toInt(),
                headBox.x2.toInt(),
                headBox.y2.toInt()
            )
            boxDrawable.draw(canvas)
        }
        return outBitmap
    }

    /**
     * 将Bitmap转换为Base64字符串
     */
    fun bitmapImgBase64(bitmap: Bitmap?): String {
        if (bitmap == null) return ""
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }

    /**
     * 获取Drawable图片
     * @param drawable  资源
     * @return          bitmap
     */
    fun getBitmap(drawable: Drawable?): Bitmap? {
        var bitmap: Bitmap? = null

        try {
            if (drawable == null) return null

            bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
            )

            val canvas = Canvas(bitmap)

            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)

            drawable.draw(canvas)
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
        }

        return bitmap
    }
}


// 辅助数据类
data class HeadBox(val x1: Float, val y1: Float, val x2: Float, val y2: Float)