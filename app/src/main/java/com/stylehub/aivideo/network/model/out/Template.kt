package com.stylehub.aivideo.network.model.out

/**
 *
 * Create by league at 2025/7/4
 *
 * Write some description here
 */
interface Template {
    fun getTmpCredits(): Int?
    fun getTmpName(): String?
    fun getTmpUrl(): String?
    fun getPreviewUrl(): String?{return getTmpUrl()}
}