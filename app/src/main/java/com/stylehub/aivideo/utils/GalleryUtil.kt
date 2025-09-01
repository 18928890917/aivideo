package com.stylehub.aivideo.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore

object GalleryUtil {
    enum class MediaType {
        IMAGE, VIDEO, IMAGE_AND_VIDEO
    }

    internal var callback: ((Uri?) -> Unit)? = null

    fun openGallery(context: Context, mediaType: MediaType, callback: (Uri?) -> Unit) {
        this.callback = callback
        val intent = Intent(context, GalleryProxyActivity::class.java)
        intent.putExtra("mediaType", mediaType.name)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        if (context is Activity) {
            context.overridePendingTransition(0, 0)
        }
    }

    fun buildGalleryIntent(mediaType: MediaType): Intent {
        return Intent(Intent.ACTION_PICK).apply {
            type = when (mediaType) {
                MediaType.IMAGE -> "image/*"
                MediaType.VIDEO -> "video/*"
                MediaType.IMAGE_AND_VIDEO -> "*/*"
            }
            putExtra(Intent.EXTRA_MIME_TYPES, when (mediaType) {
                MediaType.IMAGE -> arrayOf("image/*")
                MediaType.VIDEO -> arrayOf("video/*")
                MediaType.IMAGE_AND_VIDEO -> arrayOf("image/*", "video/*")
            })
        }
    }
}
