package com.stylehub.aivideo.ui.common

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.stylehub.aivideo.base.BaseActivity
import com.stylehub.aivideo.base.BaseViewModel
import com.stylehub.aivideo.utils.LoginManager

/**
 *
 * Create by league at 2025/7/9
 *
 * Write some description here
 */
class WebViewActivity :
    BaseActivity<WebViewActivityViewModel, WebViewActivityUiData>() {
    override val mViewModel: WebViewActivityViewModel by viewModels()

    companion object {
        const val EXTRA_URL = "extra_url"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = intent.getStringExtra(EXTRA_URL) ?: ""
        mViewModel.setUrl(url)
    }

    @Composable
    override fun ProvideContent(uiStateData: WebViewActivityUiData) {
        val url = uiStateData.url
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = true
                    val header = hashMapOf(pairs = arrayOf(Pair("user_token", LoginManager.getUserToken())))
                    loadUrl(url, header)
                }
            }
        )
    }
}

class WebViewActivityViewModel : BaseViewModel<WebViewActivityUiData>(WebViewActivityUiData()) {

    val mutableData = _uiStateData.value

    fun setUrl(url: String) {
        mutableData.url = url
    }
}

class WebViewActivityUiData {
    var url: String by mutableStateOf("")
}