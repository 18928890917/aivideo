package com.stylehub.aivideo.ui.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.facebook.FacebookSdk
import com.facebook.LoggingBehavior
import com.stylehub.aivideo.BuildConfig
import com.stylehub.aivideo.R
import com.stylehub.aivideo.base.BaseActivity
import com.stylehub.aivideo.base.BaseViewModel
import com.stylehub.aivideo.constants.PrefKey
import com.stylehub.aivideo.utils.LoginManager
import com.stylehub.aivideo.utils.ScreenUtil
import com.stylehub.aivideo.utils.SharedPreferenceUtil
import com.stylehub.aivideo.utils.ToastUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *
 * Create by league at 2025/7/23
 *
 * Write some description here
 */
class WelcomeActivity: BaseActivity<WelcomeActivityViewModel, WelcomeActivityUiData>() {
    override val mViewModel: WelcomeActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        ScreenUtil.setFullScreen(this)
        super.onCreate(savedInstanceState)
        ScreenUtil.expandToStatusBar(this)
        initInstallReferrer()
        LoginManager.initFromPreference()

        lifecycleScope.launch {
            val inMillions = System.currentTimeMillis()

            if (!LoginManager.isGoogleLogin()) {
                LoginManager.fastLogin(
                    onError = { _,
                                msg ->
                        ToastUtil.show(msg)
                    }
                )
            }
            val outMillions = System.currentTimeMillis()
            val used = outMillions - inMillions

            if (used < 4800) {
                delay(4800 - used)
            }

            if (BuildConfig.DEBUG) {
                FacebookSdk.setIsDebugEnabled(true)
                FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS)
            }

            // 跳转到HomeActivity
            startActivity(Intent(this@WelcomeActivity, HomeActivity::class.java))
            finish()
        }
    }

    private fun initInstallReferrer() {
        val referrerClient = InstallReferrerClient.newBuilder(this).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        // Connection established
                        try {
                            val response: ReferrerDetails = referrerClient.installReferrer
                            val referrerUrl = response.installReferrer
                            val clickTimestamp = response.referrerClickTimestampSeconds
                            val installTimestamp = response.installBeginTimestampSeconds
                            // 你可以在这里处理 referrer 信息，比如上报或存储
                            SharedPreferenceUtil.put(PrefKey.REFERRER_URL, referrerUrl)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        } finally {
                            referrerClient.endConnection()
                        }
                    }

                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                        // API 不支持
                    }

                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                        // 服务不可用
                    }
                }
            }

            override fun onInstallReferrerServiceDisconnected() {
                // 连接断开，可以重试
            }
        })
    }

    @Composable
    override fun ProvideContent(uiStateData: WelcomeActivityUiData) {
        // 启动页状态：0=动画，1=静图
        var splashState by remember { mutableIntStateOf(0) }

        LaunchedEffect(Unit) {
            // 播放WebP动画（假设3秒）
            delay(2800)
            splashState = 1
        }

        Box(modifier = Modifier.fillMaxSize()) {
            when (splashState) {
                0 -> {
                    // 根据API版本选择动画资源
                    val animRes = if (Build.VERSION.SDK_INT >= 28) R.drawable.anim_start_page else R.drawable.anim_start_page_low_version
                    AsyncImage(
                        model = animRes,
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                1 -> {
                    // 显示静态图
                    Image(
                        painter = painterResource(id = R.drawable.bg_start_page),
                        contentDescription = "启动静图",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

class WelcomeActivityViewModel: BaseViewModel<WelcomeActivityUiData>(WelcomeActivityUiData()) {

}

class WelcomeActivityUiData {

}