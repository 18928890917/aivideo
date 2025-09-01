package com.stylehub.aivideo.ui.template

import androidx.activity.viewModels

/**
 *
 * Create by league at 2025/7/1
 *
 * Write some description here
 */
class CommonSwapActivity : BaseSwapActivity<CommonSwapActivityViewModel, CommonPhotoSwapActivityUiData>() {
    override val mViewModel: CommonSwapActivityViewModel by viewModels()

    companion object {
        const val EXTRA_TEMPLATE = "template"
    }
}

