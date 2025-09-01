package com.stylehub.aivideo.ui.template

import android.os.Bundle
import androidx.activity.viewModels
import com.stylehub.aivideo.network.model.out.Template

/**
 *
 * Create by league at 2025/7/1
 *
 * Write some description here
 */
class VideoFaceSwapActivity : BaseSwapActivity<VideoFaceSwapActivityViewModel, VideoFaceSwapActivityUiData>() {
    override val mViewModel: VideoFaceSwapActivityViewModel by viewModels()

    companion object {
        const val EXTRA_TEMPLATE = "template"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.setIntentData(intent.getSerializableExtra(EXTRA_TEMPLATE) as Template?)
    }
}

