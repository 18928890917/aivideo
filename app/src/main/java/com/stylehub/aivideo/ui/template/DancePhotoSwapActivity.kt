package com.stylehub.aivideo.ui.template

import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.stylehub.aivideo.R
import com.stylehub.aivideo.base.BaseActivity
import com.stylehub.aivideo.network.model.out.GetImg2VideoPoseTemplateRespDataModel
import com.stylehub.aivideo.ui.dialog.ChooseFaceDialog
import com.stylehub.aivideo.utils.GalleryUtil

/**
 *
 * Create by league at 2025/7/1
 *
 * Write some description here
 */
class DancePhotoSwapActivity : BaseSwapActivity<DancePhotoSwapActivityViewModel, DancePhotoSwapActivityData>() {
    override val mViewModel: DancePhotoSwapActivityViewModel by viewModels()

    companion object {
        const val EXTRA_TEMPLATE = "template"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intentData: GetImg2VideoPoseTemplateRespDataModel = intent.getSerializableExtra(EXTRA_TEMPLATE) as GetImg2VideoPoseTemplateRespDataModel
        mViewModel.setIntentData(intentData)
    }
}