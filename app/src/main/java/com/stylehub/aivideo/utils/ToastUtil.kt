package com.stylehub.aivideo.utils

import android.content.Context
import android.widget.Toast
import com.stylehub.aivideo.AiSwapApplication

/**
 *
 * Create by league at 2025/7/4
 *
 * Write some description here
 */

object ToastUtil {

    private fun getContext(): Context {
        return AiSwapApplication.getInstance().getTopActivity() ?: AiSwapApplication.getInstance()
    }

    fun show(charSequence: CharSequence) {
        Toast.makeText(getContext(), charSequence, Toast.LENGTH_SHORT).show()
    }
}