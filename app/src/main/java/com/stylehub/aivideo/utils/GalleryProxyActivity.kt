package com.stylehub.aivideo.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class GalleryProxyActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        window.setBackgroundDrawableResource(android.R.color.transparent)

        val type = intent.getStringExtra("mediaType") ?: "IMAGE"
        val mediaType = GalleryUtil.MediaType.valueOf(type)
        val galleryIntent = GalleryUtil.buildGalleryIntent(mediaType)
        startActivityForResult(galleryIntent, 1001)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1001) {
            val uri = data?.data
            GalleryUtil.callback?.invoke(uri)
            GalleryUtil.callback = null
            finish()
            overridePendingTransition(0, 0)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
} 